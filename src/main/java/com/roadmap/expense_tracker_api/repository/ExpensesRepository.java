package com.roadmap.expense_tracker_api.repository;

import com.roadmap.expense_tracker_api.entity.ExpensesEntity;
import com.roadmap.expense_tracker_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpensesRepository extends JpaRepository<ExpensesEntity, Integer> {

    List<ExpensesEntity> findByUserAndExpenseDateBetween(
            UserEntity user,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
