package com.personal.match_time.user.service;

import com.personal.match_time.user.model.User;
import com.personal.match_time.user.repository.UserRepository;
import com.personal.match_time.user.request.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not found"));
    }

    public User storeUser(UserRequest userRequest) {

        User user = new User(userRequest);
        String password = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(password);
        user.setCreatedAt(Instant.now());

        return userRepository.save(user);
    }
}
