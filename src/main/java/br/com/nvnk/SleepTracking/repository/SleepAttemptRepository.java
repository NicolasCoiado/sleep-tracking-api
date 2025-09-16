package br.com.nvnk.SleepTracking.repository;

import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SleepAttemptRepository extends MongoRepository<SleepAttempt, String> {}
