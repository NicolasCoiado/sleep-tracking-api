package br.com.nvnk.SleepTracking.service;

import br.com.nvnk.SleepTracking.entity.User;
import br.com.nvnk.SleepTracking.exception.UserEmailAlreadyInUseException;
import br.com.nvnk.SleepTracking.exception.UserNotFoundException;
import br.com.nvnk.SleepTracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

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

    public void deleteUser (String id){
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        repository.delete(user);
    }

}