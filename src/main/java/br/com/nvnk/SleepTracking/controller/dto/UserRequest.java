package br.com.nvnk.SleepTracking.controller.dto;

import br.com.nvnk.SleepTracking.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @Size(min = 4, message = "The username must contain at least 4 characters.")
        String username,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 6, message = "The password must contain at least 6 characters.")
        String password,
        @NotNull
        UserRole userRole
){}
