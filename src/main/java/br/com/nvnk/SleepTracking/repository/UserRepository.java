package br.com.nvnk.SleepTracking.repository;

import br.com.nvnk.SleepTracking.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail (String Email);
}
