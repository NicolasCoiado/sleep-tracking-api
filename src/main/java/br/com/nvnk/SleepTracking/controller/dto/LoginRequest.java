package br.com.nvnk.SleepTracking.controller.dto;

public record LoginRequest(
        String email,
        String password
){}
