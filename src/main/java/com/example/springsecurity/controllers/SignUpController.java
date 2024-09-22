package com.example.springsecurity.controllers;

import com.example.springsecurity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @PostMapping(value = "/signup")
    public ResponseEntity addUser(@RequestParam String username, @RequestParam String password) {
        userService.signUp(username, password);
        return new ResponseEntity(HttpStatus.OK);
    }
}
