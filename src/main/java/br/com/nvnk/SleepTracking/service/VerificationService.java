package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final EmailQueueService emailQueueService; // para reenviar

    public boolean verifyRegistrationCode(String email, String code) {
        String key = "verif:register:" + email;
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return false;
        if (code.equals(value.toString())) {
            // marcar user
            User user = userRepository.findByEmail(email).orElseThrow();
            user.setEmailVerified(true);
            user.setAccountLocked(false);
            userRepository.save(user);
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    public boolean verifyPasswordResetCode(String email, String code, String newPassword, PasswordEncoder passwordEncoder) {
        String key = "verif:reset:" + email;
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return false;
        if (code.equals(value.toString())) {
            User user = userRepository.findByEmail(email).orElseThrow();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    public boolean confirmEmailChange(String userId, String code) {
        String key = "verif:change:" + userId;
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) return false;
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            String storedCode = (String) map.get("code");
            String newEmail = (String) map.get("newEmail");
            if (storedCode != null && storedCode.equals(code)) {
                User user = userRepository.findById(userId).orElseThrow();
                user.setEmail(newEmail);
                userRepository.save(user);
                redisTemplate.delete(key);
                return true;
            }
        }
        return false;
    }

    public void resendRegistrationCode(String email) {
        // pega user, obtem userId e chama emailQueueService.enqueueVerificationEmail
        User user = userRepository.findByEmail(email).orElseThrow();
        emailQueueService.enqueueVerificationEmail(user.getId(), email);
    }
}