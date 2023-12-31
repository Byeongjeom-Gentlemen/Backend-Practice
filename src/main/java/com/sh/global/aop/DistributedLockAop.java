package com.sh.global.aop;

import com.sh.global.exception.customexcpetion.CommonCustomException;
import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.util.CustomSpringELParser;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.transaction.TransactionTimeoutException;
import org.springframework.stereotype.Component;

/**
 * @DistributedLock 선언 시 수행되는 Aop Class
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.sh.global.aop.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key =
                REDISSON_LOCK_PREFIX
                        + CustomSpringELParser.getDynamicValue(
                                signature.getParameterNames(),
                                joinPoint.getArgs(),
                                distributedLock.key());
        // Lock 의 이름으로 RLock Instance 가져오기
        RLock rLock = redissonClient.getLock(key);

        try {
            // 정의된 waitTime 까지 획득을 시도하고 정의된 leaseTime 이 지나면 잠금을 해제
            boolean isLock =
                    rLock.tryLock(
                            distributedLock.waitTime(),
                            distributedLock.leaseTime(),
                            distributedLock.timeUnit());
            if (!isLock) {
                throw CommonCustomException.TRY_AGAIN_LATER;
            }

            // DistributedLock 어노테이션이 선언된 메서드를 별도의 트랜잭션으로 실행
            return aopForTransaction.proceed(joinPoint);
        } catch (CustomException | TransactionTimeoutException e) {
            log.info(e.getMessage());
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException();
        } finally {
            // case 1 (try,catch)
            try {
                // 종료 시 Lock 해제
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                // Lock leaseTime 이 종료되어 자동으로 Lock 이 해제된 후 unlock() 시도 시 발생
                log.info("Redisson Lock Already UnLock {} {}");
            }

            // case 2 (if)
            // 현재 Lock 이 잠겨있고 현재 스레드의 Lock 인 경우에만 Lock 해제
            // 이미 Lock 이 해제되었거나, 다른 스레드의 Lock 인 경우 Lock 을 해제를 시도하지 않음.
            // IllegalMonitorStateException 이 발생하지 않음.
            /*
            if(rLock.isLocked() && rLock.isHeldByCurrentThread()) {
            	rLock.unlock();
            }
             */
        }
    }
}
