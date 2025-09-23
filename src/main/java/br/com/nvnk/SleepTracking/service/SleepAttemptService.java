package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.exception.SleepAttemptInvalidException;
import br.com.nvnk.SleepTracking.exception.SleepAttemptNotFoundException;
import br.com.nvnk.SleepTracking.repository.SleepAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SleepAttemptService {

    private final SleepAttemptRepository repository;
    private final UserService userService;
    private final AuthorizationService authorizationService;

    public SleepAttempt save(SleepAttempt sleepAttempt) {

        User byId = userService.findById(sleepAttempt.getUserId());

        if (sleepAttempt.getBedTime() == null || sleepAttempt.getWakeTime() == null) {
            throw new SleepAttemptInvalidException("getBedTime and getWakeTime cannot be null.");
        }

        Duration totalTimeInBed = Duration.between(sleepAttempt.getBedTime(), sleepAttempt.getWakeTime());
        sleepAttempt.setTotalTimeInBed(totalTimeInBed);

        if (sleepAttempt.isSuccess()) {
            if (sleepAttempt.getSleepStartTime() == null || sleepAttempt.getSleepEndTime() == null) {
                throw new SleepAttemptInvalidException("SleepStartTime and SleepEndTime cannot be null if success = true.");
            }

            if (sleepAttempt.getSleepStartTime().isBefore(sleepAttempt.getBedTime())) {
                throw new SleepAttemptInvalidException("SleepStartTime cannot be before BedTime.");
            }

            if (sleepAttempt.getSleepEndTime().isAfter(sleepAttempt.getWakeTime())) {
                throw new SleepAttemptInvalidException("SleepEndTime cannot be after WakeTime.");
            }

            Duration totalSleepTime = Duration.between(sleepAttempt.getSleepStartTime(), sleepAttempt.getSleepEndTime());
            sleepAttempt.setTotalSleepTime(totalSleepTime);
        }else{
            sleepAttempt.setSleepStartTime(null);
            sleepAttempt.setSleepEndTime(null);
        }

        return repository.save(sleepAttempt);
    }

    public List<SleepAttempt> findAttemptsByUser() {
        return repository.findByUserId(authorizationService.getAuthenticatedUserId())
                .orElseThrow(() -> new SleepAttemptNotFoundException("No sleep attempts found."));

    }

    public List<SleepAttempt> findSuccessfulAttemptsByUser() {
        return repository.findByUserIdAndSuccess(authorizationService.getAuthenticatedUserId(), true)
                .orElseThrow(() -> new SleepAttemptNotFoundException("No successful sleep attempts found"));
    }

    public List<SleepAttempt> findUnsuccessfulAttemptsByUser() {
        return repository.findByUserIdAndSuccess(authorizationService.getAuthenticatedUserId(), false)
                .orElseThrow(() -> new SleepAttemptNotFoundException("No successful sleep attempts found"));
    }

}
