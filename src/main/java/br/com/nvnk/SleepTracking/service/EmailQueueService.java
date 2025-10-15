package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.dto.EmailJob;
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
        int code = new Random().nextInt(900000) + 100000; // 100000..999999
        return String.valueOf(code);
    }

    public void enqueueVerificationEmail(String userId, String email) {
        String code = generateSixDigitCode();
        // save code in redis
        String key = PREFIX_REGISTER + email; // usar email como chave
        redisTemplate.opsForValue().set(key, code, CODE_TTL);

        // prepare email job
        String subject = "Confirme sua conta";
        String body = "Seu código de verificação é: " + code + "\nValido por 15 minutos.";

        EmailJob job = new EmailJob();
        job.setTo(email);
        job.setSubject(subject);
        job.setBody(body);
        job.setType(EmailJob.Type.VERIFICATION);
        job.setUserId(userId);
        job.setMeta(Map.of("purpose", "register"));

        // push to list (LPUSH / RPUSH — vamos usar LPUSH para enfileirar)
        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, job);
    }

    public void enqueuePasswordResetEmail(String email) {
        String code = generateSixDigitCode();
        String key = "verif:reset:" + email;
        redisTemplate.opsForValue().set(key, code, CODE_TTL);

        EmailJob job = new EmailJob();
        job.setTo(email);
        job.setSubject("Recuperação de senha");
        job.setBody("Seu código para recuperação de senha é: " + code + "\nValido por 15 minutos.");
        job.setType(EmailJob.Type.PASSWORD_RESET);
        job.setMeta(Map.of("purpose", "reset"));

        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, job);
    }

    public void enqueueEmailChangeVerification(String userId, String newEmail) {
        String code = generateSixDigitCode();
        String key = "verif:change:" + userId; // ligar por userId, guardar newEmail + code
        Map<String, String> payload = Map.of("code", code, "newEmail", newEmail);
        redisTemplate.opsForValue().set(key, payload, CODE_TTL);

        EmailJob job = new EmailJob();
        job.setTo(newEmail);
        job.setSubject("Confirme seu novo e-mail");
        job.setBody("Seu código para confirmar a alteração de e-mail é: " + code + "\nValido por 15 minutos.");
        job.setType(EmailJob.Type.EMAIL_CHANGE);
        job.setUserId(userId);
        job.setMeta(Map.of("newEmail", newEmail));

        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, job);
    }
}