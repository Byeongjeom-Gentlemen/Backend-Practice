package com.sh.global.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/** Redisson Distributed Lock Annotation */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /** Lock 이름 */
    String key();

    /** Lock 시간 단위 (초) */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /** Lock 을 기다리는 시간 (default - 5초) Lock 획득을 위해 waitTime 만큼 대기 */
    long waitTime() default 5L;

    /** Lock 임대 시간 (default - 3초) Lock 을 획득한 후 leaseTime 이 지나면 해제 */
    long leaseTime() default 5L;
}
