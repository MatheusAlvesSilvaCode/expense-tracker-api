package com.roadmap.expense_tracker_api.service;

import com.roadmap.expense_tracker_api.dto.ExpensesDto;
import com.roadmap.expense_tracker_api.entity.ExpensesEntity;
import com.roadmap.expense_tracker_api.repository.ExpensesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExpensesService {

    ExpensesRepository expensesRepository;

    public ExpensesService(ExpensesRepository expensesRepository) {this.expensesRepository = expensesRepository;}

    public ExpensesEntity createExpenses(ExpensesDto dto) {
        ExpensesEntity newExpenses = new ExpensesEntity();
        newExpenses.setAmount(dto.amount()); //ok
        newExpenses.setCategoryId(dto.categoryId()); // ok
        newExpenses.setDescription(dto.description()); // ok

        return newExpenses;
    }

}
