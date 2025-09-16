package br.com.nvnk.SleepTracking.controller.dto.response;

import java.time.Duration;
import java.time.LocalDateTime;

public record SleepAttemptResponse (
        String id,
        String userId,
        boolean success,
        LocalDateTime bedTime,
        LocalDateTime wakeTime,
        LocalDateTime sleepStartTime,
        LocalDateTime sleepEndTime,
        Duration totalTimeInBed,
        Duration totalSleepTime
){}
