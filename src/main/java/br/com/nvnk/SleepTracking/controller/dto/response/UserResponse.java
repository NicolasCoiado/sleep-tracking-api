package br.com.nvnk.SleepTracking.controller.dto.response;

import br.com.nvnk.SleepTracking.entity.UserRole;

import java.time.LocalTime;

public record UserResponse(
        String id,
        String username,
        String email,
        UserRole userRole,
        LocalTime bedtimeGoal,
        LocalTime targetWakeTime
){}
