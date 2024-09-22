package com.example.springsecurity.services;

import com.example.springsecurity.entities.UserEntity;
import com.example.springsecurity.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public void signUp(@RequestParam String username, @RequestParam String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setEncryptedPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
    }
}
