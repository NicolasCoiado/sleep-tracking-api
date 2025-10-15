package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.emaildto.EmailJob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailQueueService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String EMAIL_QUEUE = "email:queue";
    private static final String PREFIX_REGISTER = "verif:register:";
    private static final Duration CODE_TTL = Duration.ofMinutes(15);

    public String generateSixDigitCode() {
        int code = new Random().nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public void enqueueVerificationEmail(String userId, String email) {
        String code = generateSixDigitCode();
        String key = PREFIX_REGISTER + email;
        redisTemplate.opsForValue().set(key, code, CODE_TTL);

        String subject = "Confirm your account";
        String body = "Your verification code is: " + code + "\nValid for 15 minutes.";

        EmailJob job = new EmailJob();
        job.setTo(email);
        job.setSubject(subject);
        job.setBody(body);
        job.setType(EmailJob.Type.VERIFICATION);
        job.setUserId(userId);
        job.setMeta(Map.of("purpose", "register"));

        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, job);
    }

    public void enqueuePasswordResetEmail(String email) {
        String code = generateSixDigitCode();
        String key = "verif:reset:" + email;
        redisTemplate.opsForValue().set(key, code, CODE_TTL);

        EmailJob job = new EmailJob();
        job.setTo(email);
        job.setSubject("Password recovery");
        job.setBody("Your password recovery code is: " + code + "\nValid for 15 minutes.");
        job.setType(EmailJob.Type.PASSWORD_RESET);
        job.setMeta(Map.of("purpose", "reset"));

        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, job);
    }

    public void enqueueEmailChangeVerification(String userId, String newEmail) {
        String code = generateSixDigitCode();
        String key = "verif:change:" + userId;
        Map<String, String> payload = Map.of("code", code, "newEmail", newEmail);
        redisTemplate.opsForValue().set(key, payload, CODE_TTL);

        EmailJob job = new EmailJob();
        job.setTo(newEmail);
        job.setSubject("Confirm your new email");
        job.setBody("Your code to confirm the email change is: " + code + "\nValid for 15 minutes.");
        job.setType(EmailJob.Type.EMAIL_CHANGE);
        job.setUserId(userId);
        job.setMeta(Map.of("newEmail", newEmail));

        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, job);
    }
}