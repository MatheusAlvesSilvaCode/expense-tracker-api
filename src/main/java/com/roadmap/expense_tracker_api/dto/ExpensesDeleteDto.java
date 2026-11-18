package com.roadmap.expense_tracker_api.dto;

import java.time.LocalDateTime;

public record DeleteExpensesDto(
        String description,
        Double amount,
        LocalDateTime expense_date,
        Integer expenseId,
        Integer categoryId) {}
