package br.com.nvnk.SleepTracking.controller.documentation;

import br.com.nvnk.SleepTracking.controller.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Tag(name = "Admin", description = "Administrative endpoints for managing users and sleep attempts.")
@SecurityRequirement(name = "bearerAuth")
public interface AdminControllerDoc {

    @Operation(summary = "List all users")
    @ApiResponse(responseCode = "200", description = "Users listed successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
    @GetMapping("/users")
    ResponseEntity<Map<String, Object>> listAllUsers();

    @Operation(summary = "Find user by ID")
    @ApiResponse(responseCode = "200", description = "User found successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/users/{id}")
    ResponseEntity<Map<String, Object>> findUser(@PathVariable String id);
}