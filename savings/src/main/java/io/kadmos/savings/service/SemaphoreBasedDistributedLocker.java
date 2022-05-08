package io.kadmos.savings.service;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import io.kadmos.savings.exception.UnableToAcquireLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SemaphoreBasedDistributedLocker {

    public static final int DEFAULT_SEMAPHORE_PERMIT_COUNT = 1;
    public static final long REMAINING_TIME_TO_LIVE_EXPIRED = -1;
    public static final long REMAINING_TIME_TO_LIVE_NOT_INITIALIZED = -2;
    public static final String UNABLE_TO_ACQUIRE_MSG_FORMAT = "Unable to acquire semaphore " + "for key: %s, it seem to be acquired by other operation";
    private final RedissonClient redissonClient;

    private void tryAcquire(RSemaphore semaphore, String key, Long leaseTime) {
        tryInitSemaphoreWithMaximumAvailablePermits(semaphore, DEFAULT_SEMAPHORE_PERMIT_COUNT);
        if (semaphore.tryAcquire()) {
            log.info("Redis semaphore acquired with key : {}", key);
            semaphore.expire(Instant.ofEpochMilli(leaseTime));
        } else {
            throw new UnableToAcquireLockException(String.format(UNABLE_TO_ACQUIRE_MSG_FORMAT, key));
        }
    }

    private void tryAcquire(RSemaphore semaphore, String key, Long leaseTime, Long waitTime) {
        tryInitSemaphoreWithMaximumAvailablePermits(semaphore, DEFAULT_SEMAPHORE_PERMIT_COUNT);
        try {
            if (semaphore.tryAcquire(waitTime, TimeUnit.MILLISECONDS)) {
                log.info("Redis semaphore acquired with key : {}", key);
                semaphore.expire(Instant.ofEpochMilli(leaseTime));
            } else {
                throw new UnableToAcquireLockException(String.format(UNABLE_TO_ACQUIRE_MSG_FORMAT, key));
            }
        } catch (InterruptedException e) {
            throw new UnableToAcquireLockException(String.format(UNABLE_TO_ACQUIRE_MSG_FORMAT, key));
        }
    }

    public void acquire(String key) {
        acquire(key, 0L);
    }

    public void acquire(String key, Long leaseTime) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        tryAcquire(semaphore, key, leaseTime);
    }

    public void acquire(String key, Long leaseTime, Long waitTime) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        tryAcquire(semaphore, key, leaseTime, waitTime);
    }

    public void release(String key) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        if (null == semaphore) {
            log.warn("There is no semaphore created with key: [{}] to be cleared", key);
            return;
        }
        tryDrainPermits(semaphore, key);
        semaphore.clearExpire();
        semaphore.release();
        log.info("Redis semaphore released with key: {}", key);
    }

    public boolean isLocked(String key) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        return null != semaphore && semaphore.isExists();
    }


    /**
     * This method adds available permits only if semaphore was not initialized or is expired.
     *
     * @param semaphore   a semaphore object to initialize
     * @param permitCount amount of permits to add
     */
    private void tryInitSemaphoreWithMaximumAvailablePermits(RSemaphore semaphore, int permitCount) {
        if (isExpiredOrNonInitialized(semaphore)) {
            semaphore.trySetPermits(permitCount);
        }
    }

    /**
     * This method checks if there is an unusual situation when there are more that zero(max)
     * permits available when trying to release a lock. In this  case it will try to reduce the
     * amount of permits to zero.
     *
     * @param semaphore a semaphore object to check
     */
    private void tryDrainPermits(RSemaphore semaphore, String key) {
        int availablePermits = semaphore.availablePermits();
        if (semaphore.availablePermits() >= DEFAULT_SEMAPHORE_PERMIT_COUNT) {
            log.warn("There are more than zero available permits when trying to release a lock, key: {}, count: {}", key, availablePermits);
            semaphore.drainPermits();
        }
    }

    /**
     * This method checks if semaphore is expired or wasn't initialized. It is used for setting
     * maximum available permits to semaphore.
     *
     * @param semaphore a semaphore object to check
     * @return true if semaphore was not initialized or its TTL is expired
     */
    private boolean isExpiredOrNonInitialized(RSemaphore semaphore) {
        long remainingTTl = semaphore.remainTimeToLive();
        return (remainingTTl == REMAINING_TIME_TO_LIVE_EXPIRED) || (remainingTTl == REMAINING_TIME_TO_LIVE_NOT_INITIALIZED);
    }

}

