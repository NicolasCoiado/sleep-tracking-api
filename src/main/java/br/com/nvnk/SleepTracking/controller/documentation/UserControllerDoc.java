package br.com.nvnk.SleepTracking.controller.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Tag(name = "User", description = "Endpoints for user account management.")
@SecurityRequirement(name = "bearerAuth")
public interface UserControllerDoc {

    @Operation(summary = "Describe user", description = "Returns the logged-in user's information.")
    @GetMapping
    ResponseEntity<Map<String, Object>> describeUser();

    @Operation(summary = "Calculate ideal bedtime", description = "Calculates the user's ideal bedtime based on sleep data.")
    @GetMapping("calculate/ideal-bedtime")
    ResponseEntity<Map<String, Object>> calculateIdealBedtime();
}