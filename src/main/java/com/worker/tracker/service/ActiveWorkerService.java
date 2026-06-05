package com.worker.tracker.service;

import com.worker.tracker.dto.ActiveWorkerData;
import com.worker.tracker.dto.ActiveWorkerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ActiveWorkerService {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    public List<ActiveWorkerResponse> getActiveWorkers() {

        List<ActiveWorkerResponse> result =
                new ArrayList<>();

        if (redisTemplate == null) {
            return result;
        }

        try {

            Set<String> keys =
                    redisTemplate.keys("active:worker:*");

            if (keys == null) {
                return result;
            }

            for (String key : keys) {

                Object obj =
                        redisTemplate.opsForValue().get(key);

                if (obj == null) {
                    continue;
                }

                ActiveWorkerData data =
                        (ActiveWorkerData) obj;

                Long workerId =
                        Long.valueOf(key.split(":")[2]);

                result.add(
                        new ActiveWorkerResponse(
                                workerId,
                                data.getSiteId(),
                                data.getClockInTime()
                        )
                );
            }

        } catch (Exception ignored) {
        }

        return result;
    }
}