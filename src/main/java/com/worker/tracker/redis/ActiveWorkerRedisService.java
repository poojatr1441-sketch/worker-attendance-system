package com.worker.tracker.redis;

import com.worker.tracker.dto.ActiveWorkerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ActiveWorkerRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ACTIVE_SET = "active:workers";
    private static final String ACTIVE_DATA = "active:worker:data";

    private String key(Long workerId) {
        return "active:worker:" + workerId;
    }

    public void clockIn(Long workerId, ActiveWorkerData data) {

        try {
            // 1. Set active flag
            redisTemplate.opsForSet().add(ACTIVE_SET, workerId);

            // 2. Store worker data
            redisTemplate.opsForHash().put(
                    ACTIVE_DATA,
                    workerId.toString(),
                    data
            );

            // 3. TTL safety net (required by assignment)
            redisTemplate.opsForValue().set(
                    key(workerId),
                    data,
                    Duration.ofHours(16)
            );

        } catch (Exception ignored) {}
    }

    public ActiveWorkerData get(Long workerId) {

        try {
            Object obj = redisTemplate.opsForHash()
                    .get(ACTIVE_DATA, workerId.toString());

            return (ActiveWorkerData) obj;

        } catch (Exception e) {
            return null;
        }
    }

    public void clockOut(Long workerId) {

        try {
            redisTemplate.opsForSet().remove(ACTIVE_SET, workerId);
            redisTemplate.opsForHash().delete(ACTIVE_DATA, workerId.toString());
            redisTemplate.delete(key(workerId));

        } catch (Exception ignored) {}
    }

    public boolean isActive(Long workerId) {

        try {
            return Boolean.TRUE.equals(
                    redisTemplate.opsForSet().isMember(ACTIVE_SET, workerId)
            );
        } catch (Exception e) {
            return false;
        }
    }

    public void invalidateWorker(Long workerId) {

        try {
            clockOut(workerId);
        } catch (Exception ignored) {}
    }
}