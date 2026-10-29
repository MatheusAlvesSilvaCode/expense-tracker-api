package com.roadmap.expense_tracker_api.dto;

import java.time.LocalDateTime;

public record ExpensesDto(
        int id,
        String userId,
        String categoryId,
        String description,
        Double amount,
        LocalDateTime expenseDate,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {}
