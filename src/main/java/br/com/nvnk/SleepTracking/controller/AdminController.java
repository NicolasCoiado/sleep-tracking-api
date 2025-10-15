package br.com.nvnk.SleepTracking.controller;

import br.com.nvnk.SleepTracking.controller.documentation.AdminControllerDoc;
import br.com.nvnk.SleepTracking.controller.dto.response.UserResponse;
import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.mapper.UserMapper;
import br.com.nvnk.SleepTracking.service.SleepAttemptService;
import br.com.nvnk.SleepTracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController implements AdminControllerDoc {

    private final UserService userService;
    private final SleepAttemptService sleepAttemptService;

    // ROUTES FOR MANAGING USERS:

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> listAllUsers() {
        List<UserResponse> users = userService.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Users listed successfully.");
        response.put("Users", users);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> findUser (@PathVariable String id){
        User user = userService.findById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("Message", "User found.");
        response.put("Users", user);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/{id}/lock")
    public ResponseEntity<Map<String, String>> lockUser(@PathVariable String id) {
        userService.lockUser(id);
        return ResponseEntity.ok(Map.of("Message", "User locked successfully."));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("Message", "User deleted successfully."));
    }

    // ROUTES FOR MANAGING SLEEP ATTEMPTS:

    @GetMapping("/sleeps/{id}")
    public ResponseEntity<Map<String, Object>> findSleepAttemptsFromUser(@PathVariable String id){
        List<SleepAttempt> attemptsByUser = sleepAttemptService.findAttemptsByUserId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("Message", "");
        response.put("Attempts ", attemptsByUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sleeps/{id}")
    public ResponseEntity<Map<String, String>> deleteAllSleepAttemptsFromUser(@PathVariable String id) {
        sleepAttemptService.deleteAllAttemptsFromUser(id);
        return ResponseEntity.ok(Map.of("Message", "User's Sleep Attempts have been deleted."));
    }

}
