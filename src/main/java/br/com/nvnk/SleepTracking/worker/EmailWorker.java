package br.com.nvnk.SleepTracking.worker;

import br.com.nvnk.SleepTracking.dto.EmailJob;
import br.com.nvnk.SleepTracking.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailWorker {

    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailSenderService emailSenderService;
    private static final String EMAIL_QUEUE = "email:queue";

    @Scheduled(fixedDelay = 2000)
    public void processQueue() {
        Object jobObj = redisTemplate.opsForList().rightPop(EMAIL_QUEUE);
        if (jobObj == null) return;
        EmailJob job = (EmailJob) jobObj;
        try {
            emailSenderService.sendSimple(job.getTo(), job.getSubject(), job.getBody());
            // log sucesso
        } catch (Exception e) {
            // log erro — opcional: re-enfileirar ou salvar para retry
            // Exemplo: redisTemplate.opsForList().leftPush(EMAIL_QUEUE, job);
        }
    }
}