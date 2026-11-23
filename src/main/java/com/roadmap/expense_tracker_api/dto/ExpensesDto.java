package com.roadmap.expense_tracker_api.dto;

import java.time.LocalDateTime;

public record ExpensesDto(
        Integer id,
        Long userId,
        Integer categoryId,
        String description,
        Double amount,
        LocalDateTime expenseDate,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {}
