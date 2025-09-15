package br.com.nvnk.SleepTracking.mapper;

import br.com.nvnk.SleepTracking.controller.dto.UserRequest;
import br.com.nvnk.SleepTracking.controller.dto.UserResponse;
import br.com.nvnk.SleepTracking.entity.User;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .userRole(request.userRole())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole()
        );
    }
}
