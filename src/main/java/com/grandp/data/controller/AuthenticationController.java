package com.grandp.data.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.grandp.data.config.UserAuthenticationProvider;
import com.grandp.data.dto.SignUpDto;
import com.grandp.data.dto.UserDto;
import com.grandp.data.services.UserServiceFromSomeLesson;

import jakarta.validation.Valid;

@RestController
//@RequestMapping("/v1")
public class AuthenticationController {

    private final UserServiceFromSomeLesson userServiceFromSomeLesson;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public AuthenticationController(UserServiceFromSomeLesson userServiceFromSomeLesson,
                                    UserAuthenticationProvider userAuthenticationProvider) {
        this.userServiceFromSomeLesson = userServiceFromSomeLesson;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@AuthenticationPrincipal UserDto user) {
    	System.out.println("in authentication controller");
        user.setToken(userAuthenticationProvider.createToken(user.getLogin()));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signUp") //register i think
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userServiceFromSomeLesson.signUp(user);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId() + "/profile")).body(createdUser);
    }

    @PostMapping("/signOut")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDto user) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
