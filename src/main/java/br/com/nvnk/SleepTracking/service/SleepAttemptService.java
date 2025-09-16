package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import br.com.nvnk.SleepTracking.repository.SleepAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SleepAttemptService {

    private final SleepAttemptRepository repository;
    private final UserService userService;

    public SleepAttempt save (SleepAttempt sleepAttempt){

        userService.findById(sleepAttempt.getUserId());

        Duration totalTimeInBed = Duration.between(sleepAttempt.getBedTime(), sleepAttempt.getWakeTime());
        Duration totalSleepTime = Duration.between(sleepAttempt.getSleepStartTime(), sleepAttempt.getSleepEndTime());

        sleepAttempt.setTotalTimeInBed(totalTimeInBed);
        sleepAttempt.setTotalSleepTime(totalSleepTime);

        return repository.save(sleepAttempt);
    }

}
