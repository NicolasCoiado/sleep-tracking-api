package br.com.nvnk.SleepTracking.controller.dto;

import br.com.nvnk.SleepTracking.entity.UserRole;

public record UserResponse(
        String id,
        String username,
        String email,
        UserRole userRole
){}
