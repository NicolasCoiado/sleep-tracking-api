package br.com.nvnk.SleepTracking.controller;

import br.com.nvnk.SleepTracking.service.EmailQueueService;
import br.com.nvnk.SleepTracking.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final EmailQueueService emailQueueService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        boolean ok = verificationService.verifyRegistrationCode(email, code);
        if (ok) return ResponseEntity.ok(Map.of("message", "Email verified"));
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired code"));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resend(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        emailQueueService.enqueueVerificationEmail(null, email); // userId optional if unknown
        return ResponseEntity.ok(Map.of("message", "Verification email resent"));
    }

    @PostMapping("/reset-request")
    public ResponseEntity<?> requestReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        emailQueueService.enqueuePasswordResetEmail(email);
        return ResponseEntity.ok(Map.of("message", "Password reset email sent"));
    }

    @PostMapping("/reset-confirm")
    public ResponseEntity<?> confirmReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        String newPassword = body.get("newPassword");
        boolean ok = verificationService.verifyPasswordResetCode(email, code, newPassword, passwordEncoder);
        if (ok) return ResponseEntity.ok(Map.of("message", "Password updated"));
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired code"));
    }

    @PostMapping("/change-email-request")
    public ResponseEntity<?> requestEmailChange(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        String newEmail = body.get("newEmail");
        // ideally authorize userId == auth user
        emailQueueService.enqueueEmailChangeVerification(userId, newEmail);
        return ResponseEntity.ok(Map.of("message", "Email change verification sent"));
    }

    @PostMapping("/change-email-confirm")
    public ResponseEntity<?> confirmEmailChange(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        String code = body.get("code");
        boolean ok = verificationService.confirmEmailChange(userId, code);
        if (ok) return ResponseEntity.ok(Map.of("message", "Email changed"));
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired code"));
    }
}