package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.controller.dto.request.SleepAttemptRequest;
import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.exception.SleepAttemptInvalidException;
import br.com.nvnk.SleepTracking.exception.SleepAttemptNotFoundException;
import br.com.nvnk.SleepTracking.repository.SleepAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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

    public List<SleepAttempt> findAttemptsByDayAndUser(LocalDate date) {
        String userId = authorizationService.getAuthenticatedUserId();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return repository.findByUserIdAndBedTimeBetween(userId, startOfDay, endOfDay)
                .orElseThrow(() -> new SleepAttemptNotFoundException("No sleep attempts found for " + date));
    }

    public List<SleepAttempt> findAttemptsByMonthAndUser(int year, int month) {
        String userId = authorizationService.getAuthenticatedUserId();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        LocalDateTime startOfMonth = start.atStartOfDay();
        LocalDateTime endOfMonth = end.atTime(LocalTime.MAX);

        return repository.findByUserIdAndBedTimeBetween(userId, startOfMonth, endOfMonth)
                .orElseThrow(() -> new SleepAttemptNotFoundException("No attempts found for " + year + "-" + month));
    }

}
