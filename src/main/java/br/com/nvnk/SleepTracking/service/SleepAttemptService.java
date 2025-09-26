package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.controller.dto.request.SleepAttemptRequest;
import br.com.nvnk.SleepTracking.entity.SleepAttempt;
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


    // GENERAL METHODS:

    public SleepAttempt save(SleepAttempt sleepAttempt) {

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

        sleepAttempt.setUserId(authorizationService.getAuthenticatedUserId());

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

    public List<SleepAttempt> findAttemptsByRangeAndUser(LocalDate start, LocalDate end) {
        String userId = authorizationService.getAuthenticatedUserId();

        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        return repository.findByUserIdAndBedTimeBetween(userId, startDate, endDate)
                .orElseThrow(() -> new SleepAttemptNotFoundException("No attempts found from " + start + " to " + end));
    }

    public SleepAttempt getSleepAttemptById(String id) {
        String userId = authorizationService.getAuthenticatedUserId();

        return repository.findById(id)
                .filter(attempt -> attempt.getUserId().equals(userId))
                .orElseThrow(() -> new SleepAttemptNotFoundException("Sleep attempt not found with id " + id));
    }

    public SleepAttempt updateSleepAttempt(String id, SleepAttemptRequest request) {
        SleepAttempt attempt = getSleepAttemptById(id);

        attempt.setBedTime(request.bedTime());
        attempt.setWakeTime(request.wakeTime());
        attempt.setSuccess(request.success());
        attempt.setSleepStartTime(request.sleepStartTime());
        attempt.setSleepEndTime(request.sleepEndTime());

        return repository.save(attempt);
    }

    public SleepAttempt editSleepAttempt(String id, Map<String, Object> fields) {
        SleepAttempt attempt = getSleepAttemptById(id);

        fields.forEach((key, value) -> {
            switch (key) {
                case "bedTime" -> attempt.setBedTime(LocalDateTime.parse(value.toString()));
                case "wakeTime" -> attempt.setWakeTime(LocalDateTime.parse(value.toString()));
                case "success" -> attempt.setSuccess(Boolean.parseBoolean(value.toString()));
                case "sleepStartTime" -> attempt.setSleepStartTime(LocalDateTime.parse(value.toString()));
                case "sleepEndTime" -> attempt.setSleepEndTime(LocalDateTime.parse(value.toString()));
                case "totalTimeInBed" -> attempt.setTotalTimeInBed(Duration.parse(value.toString()));
                case "totalSleepTime" -> attempt.setTotalSleepTime(Duration.parse(value.toString()));
            }
        });

        return repository.save(attempt);
    }

    public void deleteSleepAttempt(String id) {
        SleepAttempt attempt = getSleepAttemptById(id);
        repository.delete(attempt);
    }

    // ADMINISTRATOR METHODS:

    public void deleteAllAttemptsFromUser(String id) {
        List<SleepAttempt> userSleepAttempts = repository.findByUserId(id)
                .orElseThrow(() -> new SleepAttemptNotFoundException("This user has no Sleep Attempts."));

        repository.deleteAll(userSleepAttempts);
    }

    public List<SleepAttempt> findAttemptsByUserId(String id) {
        return repository.findByUserId(id)
                .orElseThrow(() -> new SleepAttemptNotFoundException("No sleep attempts found."));
    }

}
