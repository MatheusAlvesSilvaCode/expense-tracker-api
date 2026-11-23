package com.roadmap.expense_tracker_api.controller;

import com.roadmap.expense_tracker_api.dto.LoginDto;
import com.roadmap.expense_tracker_api.entity.UserEntity;
import com.roadmap.expense_tracker_api.repository.UserRepository;
import com.roadmap.expense_tracker_api.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("login")
    // Find user by email
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        UserEntity user = userRepository.findByEmailUser(loginDto.email())
                .orElseThrow(() -> new RuntimeException("User invalid!"));

    if (passwordEncoder.matches(loginDto.password(), user.getPassword())) {
        String token = jwtService.generateToken(loginDto.email());
        log.info("Authentication ok!");
        return ResponseEntity.ok(token);
    }
        return ResponseEntity.status(401).body("password invalid!");
    }
}
