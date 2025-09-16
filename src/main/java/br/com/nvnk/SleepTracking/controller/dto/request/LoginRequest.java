package br.com.nvnk.SleepTracking.controller.dto.request;

public record LoginRequest(
        String email,
        String password
){}
