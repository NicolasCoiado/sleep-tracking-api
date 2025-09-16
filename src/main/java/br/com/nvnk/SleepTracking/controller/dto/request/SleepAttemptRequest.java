package br.com.nvnk.SleepTracking.controller.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SleepAttemptRequest(
        @NotNull
        String userId,
        @NotNull
        boolean success,
        LocalDateTime bedTime,
        LocalDateTime wakeTime,
        LocalDateTime sleepStartTime,
        LocalDateTime sleepEndTime
) {}
