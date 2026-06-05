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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<ActiveWorkerResponse> getActiveWorkers() {

        List<ActiveWorkerResponse> result = new ArrayList<>();

        try {
            Set<Object> workerIds =
                    redisTemplate.opsForSet().members("active:workers");

            if (workerIds == null) return result;

            for (Object idObj : workerIds) {

                Long workerId = Long.valueOf(idObj.toString());

                Object obj = redisTemplate.opsForHash()
                        .get("active:worker:data", workerId.toString());

                if (obj == null) continue;

                ActiveWorkerData data = (ActiveWorkerData) obj;

                result.add(new ActiveWorkerResponse(
                        workerId,
                        data.getSiteId(),
                        data.getClockInTime()
                ));
            }

        } catch (Exception ignored) {}

        return result;
    }
}