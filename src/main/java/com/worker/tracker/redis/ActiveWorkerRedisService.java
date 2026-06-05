package com.worker.tracker.redis;

import com.worker.tracker.dto.ActiveWorkerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ActiveWorkerRedisService {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    private String key(Long workerId) {
        return "active:worker:" + workerId;
    }

    public void clockIn(Long workerId, ActiveWorkerData data) {

        if (redisTemplate == null) {
            return;
        }

        try {
            redisTemplate.opsForValue().set(
                    key(workerId),
                    data,
                    Duration.ofHours(16)
            );
        } catch (Exception ignored) {
        }
    }

    public ActiveWorkerData get(Long workerId) {

        if (redisTemplate == null) {
            return null;
        }

        try {
            return (ActiveWorkerData)
                    redisTemplate.opsForValue().get(key(workerId));
        } catch (Exception e) {
            return null;
        }
    }

    public void clockOut(Long workerId) {

        if (redisTemplate == null) {
            return;
        }

        try {
            redisTemplate.delete(key(workerId));
        } catch (Exception ignored) {
        }
    }

    public boolean isActive(Long workerId) {

        if (redisTemplate == null) {
            return false;
        }

        try {
            return Boolean.TRUE.equals(
                    redisTemplate.hasKey(key(workerId))
            );
        } catch (Exception e) {
            return false;
        }
    }

    public void invalidateWorker(Long workerId) {

        if (redisTemplate == null) {
            return;
        }

        try {
            redisTemplate.delete(key(workerId));
        } catch (Exception ignored) {
        }
    }
}