package com.roadmap.expense_tracker_api.service;

import com.roadmap.expense_tracker_api.dto.UserDto;
import com.roadmap.expense_tracker_api.entity.ExpensesEntity;
import com.roadmap.expense_tracker_api.entity.UserEntity;
import com.roadmap.expense_tracker_api.repository.UserRepository;
import jakarta.persistence.EntityListeners;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserEntity createUser(UserDto dto){
        UserEntity userEntity = new UserEntity();

        userEntity.setNameUser(dto.nameUser());
        userEntity.setEmailUser(dto.emailUser());
        userEntity.setPassword(passwordEncoder.encode(dto.password()));
        log.info("User created");

        return userRepository.save(userEntity);
    }
}
