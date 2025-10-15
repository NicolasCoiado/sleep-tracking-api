package br.com.nvnk.SleepTracking.controller.documentation;

import br.com.nvnk.SleepTracking.controller.dto.request.SleepAttemptRequest;
import br.com.nvnk.SleepTracking.controller.dto.response.SleepAttemptResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Tag(name = "Sleep", description = "Endpoints for managing sleep attempts.")
@SecurityRequirement(name = "bearerAuth")
public interface SleepControllerDoc {

    @Operation(summary = "Register sleep attempt", description = "Registers a new sleep attempt.")
    @ApiResponse(responseCode = "201", description = "Sleep attempt saved successfully")
    @PostMapping
    ResponseEntity<Map<String, Object>> registerAttempt(@RequestBody SleepAttemptRequest request);

    @Operation(summary = "List all attempts", description = "Returns all sleep attempts from the logged-in user.")
    @ApiResponse(responseCode = "200", description = "Sleep attempts listed successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SleepAttemptResponse.class))))
    @GetMapping
    ResponseEntity<Map<String, Object>> findAttemptsByUser();

    @Operation(summary = "Get attempts by day", description = "Returns all sleep attempts from a specific date.")
    @ApiResponse(responseCode = "200", description = "Sleep attempts found")
    @GetMapping("/by-day")
    ResponseEntity<Map<String, Object>> findAttemptsByDay(@RequestParam("date") LocalDate date);
}