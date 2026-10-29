package com.roadmap.expense_tracker_api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDto(
        Integer id,
        String nameUser,
        String emailUser,
        String password,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
