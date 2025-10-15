package br.com.nvnk.SleepTracking.controller.documentation;

import br.com.nvnk.SleepTracking.controller.dto.request.LoginRequest;
import br.com.nvnk.SleepTracking.controller.dto.request.UserRequest;
import br.com.nvnk.SleepTracking.controller.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "Authentication", description = "Endpoints responsible for user authentication and registration.")
public interface AuthControllerDoc {

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user and sends a verification email."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User successfully registered",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/register")
    ResponseEntity<Map<String, Object>> register(@RequestBody UserRequest request);

    @Operation(
            summary = "Authenticate a user",
            description = "Logs in with email and password and returns a JWT token."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(schema = @Schema(implementation = Map.class))
    )
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/login")
    ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request);
}
