package com.example.springsecurity.services;

import com.example.springsecurity.entities.UserEntity;
import com.example.springsecurity.repositories.RolesEntityRepository;
import com.example.springsecurity.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RolesEntityRepository roleRepository;

    public void signUp(String username, String firstName, String lastName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEncryptedPassword(passwordEncoder.encode(password));
        userEntity.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));
        userRepository.save(userEntity);
    }
}
