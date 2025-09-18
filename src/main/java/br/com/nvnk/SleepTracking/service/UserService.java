package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.exception.UserEmailAlreadyInUseException;
import br.com.nvnk.SleepTracking.exception.UserNotFoundException;
import br.com.nvnk.SleepTracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User save (User user){
        if (repository.findByEmail(user.getEmail()).isPresent()){
            throw new UserEmailAlreadyInUseException(("This email address is already in use."));
        }

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return repository.save(user);
    }

    public User findByEmail (String email){
        return repository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found."));
    }

    public User findById (String id){
        return repository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found."));
    }

    public User describeUser (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String userId = user.getId();

        return repository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found."));
    }

}