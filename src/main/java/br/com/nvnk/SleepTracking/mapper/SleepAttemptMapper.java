package br.com.nvnk.SleepTracking.mapper;

import br.com.nvnk.SleepTracking.controller.dto.request.SleepAttemptRequest;
import br.com.nvnk.SleepTracking.controller.dto.response.SleepAttemptResponse;
import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import org.springframework.stereotype.Component;

@Component
public class SleepAttemptMapper {
    public static SleepAttempt toEntity (SleepAttemptRequest sleepAttemptRequest) {

        SleepAttempt sleepAttempt = new SleepAttempt();

        sleepAttempt.setUserId(sleepAttemptRequest.userId());
        sleepAttempt.setSuccess(sleepAttemptRequest.success());
        sleepAttempt.setBedTime(sleepAttemptRequest.bedTime());
        sleepAttempt.setWakeTime(sleepAttemptRequest.wakeTime());
        sleepAttempt.setSleepStartTime(sleepAttemptRequest.sleepStartTime());
        sleepAttempt.setSleepEndTime(sleepAttemptRequest.sleepEndTime());

        return sleepAttempt;
    }

    public static SleepAttemptResponse toResponse(SleepAttempt sleepAttempt) {
        return new SleepAttemptResponse(
                sleepAttempt.getId(),
                sleepAttempt.getUserId(),
                sleepAttempt.isSuccess(),
                sleepAttempt.getBedTime(),
                sleepAttempt.getWakeTime(),
                sleepAttempt.getSleepStartTime(),
                sleepAttempt.getSleepEndTime(),
                sleepAttempt.getTotalTimeInBed(),
                sleepAttempt.getTotalSleepTime()
        );
    }
}
