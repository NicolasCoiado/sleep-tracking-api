package br.com.nvnk.SleepTracking.repository;

import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SleepAttemptRepository extends MongoRepository<SleepAttempt, String> {
    Optional<List<SleepAttempt>> findByUserId (String userId);
    Optional<List<SleepAttempt>> findByUserIdAndSuccess(String userId, boolean success);
}
