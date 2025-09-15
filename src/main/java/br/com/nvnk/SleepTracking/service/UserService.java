package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.exception.UserEmailAlreadyInUse;
import br.com.nvnk.SleepTracking.exception.UserNotFoundException;
import br.com.nvnk.SleepTracking.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User save (User user){
        if (repository.findByEmail(user.getEmail()).isPresent()){
            throw new UserEmailAlreadyInUse(("This email address is already in use."));
        }

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return repository.save(user);
    }

    public User findByEmail (String email){
        return repository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("Username not found."));
    }


}
