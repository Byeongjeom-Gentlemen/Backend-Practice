package com.sh.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** AOP 에서 트랜잭션 분리를 위한 클래스 */
@Component
public class AopForTransaction {

    // 락을 제어하는 커넥션과 비즈니스 로직을 위한 커넥션을 분리
    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 4)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
