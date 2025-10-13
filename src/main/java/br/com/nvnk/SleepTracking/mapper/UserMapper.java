package br.com.nvnk.SleepTracking.mapper;

import br.com.nvnk.SleepTracking.controller.dto.request.UserRequest;
import br.com.nvnk.SleepTracking.controller.dto.response.UserResponse;
import br.com.nvnk.SleepTracking.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static User toEntity(UserRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .userRole(request.userRole())
                .bedtimeGoal(request.bedtimeGoal())
                .targetWakeTime(request.targetWakeTime())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserRole(),
                user.getBedtimeGoal(),
                user.getTargetWakeTime()
        );
    }
}
