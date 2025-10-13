package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.exception.SleepAttemptInvalidException;
import br.com.nvnk.SleepTracking.exception.SleepAttemptNotFoundException;
import br.com.nvnk.SleepTracking.exception.UserEmailAlreadyInUseException;
import br.com.nvnk.SleepTracking.exception.UserNotFoundException;
import br.com.nvnk.SleepTracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;
    private final SleepAttemptService sleepAttemptService;

    public User save (User user){
        if (repository.findByEmail(user.getEmail()).isPresent()){
            throw new UserEmailAlreadyInUseException(("This email address is already in use."));
        }

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return repository.save(user);
    }

    public User findById (String id){
        return repository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found."));
    }

    public List<User> findAll (){
        return repository.findAll();
    }

    public void lockUser(String id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setAccountLocked(true);
        repository.save(user);
    }

    public User describeUser (){
        String userId = authorizationService.getAuthenticatedUserId();
        return repository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found."));
    }

    public LocalTime calculateIdealBedtime() {
        String userId = authorizationService.getAuthenticatedUserId();

        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<SleepAttempt> attempts = sleepAttemptService.findByUserIdAndBedTimeBetween(
                userId,
                yesterday.atStartOfDay(),
                yesterday.atTime(LocalTime.MAX)
        ).orElseThrow(() -> new SleepAttemptNotFoundException("No sleep attempts found for yesterday."));

        Duration totalTimeInBed = attempts.stream()
                .map(SleepAttempt::getTotalTimeInBed)
                .filter(duration -> duration != null)
                .reduce(Duration.ZERO, Duration::plus);

        Duration totalSleepTime = attempts.stream()
                .filter(SleepAttempt::isSuccess)
                .map(SleepAttempt::getTotalSleepTime)
                .filter(duration -> duration != null)
                .reduce(Duration.ZERO, Duration::plus);

        if (totalTimeInBed.isZero() || totalTimeInBed.isNegative()) {
            throw new SleepAttemptInvalidException("No valid bed time data for yesterday.");
        }

        double efficiency = (double) totalSleepTime.toMinutes() / totalTimeInBed.toMinutes();

        SleepAttempt lastSuccessfulAttempt = attempts.stream()
                .filter(SleepAttempt::isSuccess)
                .max(Comparator.comparing(SleepAttempt::getBedTime))
                .orElse(null);

        if (lastSuccessfulAttempt == null) {
            throw new SleepAttemptInvalidException("No successful sleep attempts found for yesterday.");
        }

        LocalTime lastBedtime = lastSuccessfulAttempt.getBedTime().toLocalTime();

        if (efficiency >= 0.8) {
            return lastBedtime;
        } else {
            return lastBedtime.plusMinutes(15);
        }
    }

    public void deleteUser (String id){
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        repository.delete(user);
    }

}