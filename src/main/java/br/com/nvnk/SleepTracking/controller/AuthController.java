package br.com.nvnk.SleepTracking.controller;

import br.com.nvnk.SleepTracking.controller.dto.LoginRequest;
import br.com.nvnk.SleepTracking.controller.dto.UserRequest;
import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.mapper.UserMapper;
import br.com.nvnk.SleepTracking.security.TokenService;
import br.com.nvnk.SleepTracking.service.AuthorizationService;
import br.com.nvnk.SleepTracking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping ("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final AuthorizationService authorizationService;


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register (@Valid @RequestBody UserRequest userRequest){
        User user = UserMapper.toEntity(userRequest);

        User savedUser = userService.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "The user has been successfully registered.");
        response.put("User details", UserMapper.toResponse(savedUser));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login (@Valid @RequestBody LoginRequest loginRequest){
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());


        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Login successful.");
        response.put("Token", token);

        return ResponseEntity.ok(response);
    }
}
