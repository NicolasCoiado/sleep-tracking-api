package br.com.nvnk.SleepTracking.controller.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "Verification", description = "Endpoints responsible for email verification and password reset.")
public interface VerificationControllerDoc {

    @Operation(
            summary = "Verify account",
            description = "Verifies the user's email using a code sent by email."
    )
    @ApiResponse(responseCode = "200", description = "Email verified successfully")
    @ApiResponse(responseCode = "400", description = "Invalid or expired code")
    @PostMapping("/verify")
    ResponseEntity<?> verify(@RequestBody Map<String, String> body);

    @Operation(
            summary = "Resend verification email",
            description = "Sends a new verification email to the user."
    )
    @ApiResponse(responseCode = "200", description = "Verification email resent")
    @PostMapping("/resend-verification")
    ResponseEntity<?> resend(@RequestBody Map<String, String> body);

    @Operation(
            summary = "Request password reset",
            description = "Sends a password reset email to the user."
    )
    @ApiResponse(responseCode = "200", description = "Password reset email sent")
    @PostMapping("/reset-request")
    ResponseEntity<?> requestReset(@RequestBody Map<String, String> body);

    @Operation(
            summary = "Confirm password reset",
            description = "Validates the reset code and updates the password."
    )
    @ApiResponse(responseCode = "200", description = "Password updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid or expired code")
    @PostMapping("/reset-confirm")
    ResponseEntity<?> confirmReset(@RequestBody Map<String, String> body);
}