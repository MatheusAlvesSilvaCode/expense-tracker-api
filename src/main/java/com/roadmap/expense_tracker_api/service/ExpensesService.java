package com.roadmap.expense_tracker_api.service;

import com.roadmap.expense_tracker_api.dto.ExpenseUpdateDto;
import com.roadmap.expense_tracker_api.dto.ExpensesDto;
import com.roadmap.expense_tracker_api.entity.CategoriesEntity;
import com.roadmap.expense_tracker_api.entity.ExpensesEntity;
import com.roadmap.expense_tracker_api.entity.UserEntity;
import com.roadmap.expense_tracker_api.repository.CategoriesRepository;
import com.roadmap.expense_tracker_api.repository.ExpensesRepository;
import com.roadmap.expense_tracker_api.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ExpensesService {

    public final ExpensesRepository expensesRepository;
    private final CategoriesRepository categoriesRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public ExpensesService(ExpensesRepository expensesRepository,
                           CategoriesRepository categoriesRepository,
                           JwtService jwtService,
                           UserRepository userRepository) {
        this.expensesRepository = expensesRepository;
        this.categoriesRepository = categoriesRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public ExpensesEntity createExpenses(String token , ExpensesDto dto) {
        CategoriesEntity category = categoriesRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String email = jwtService.extractEmail(token);

        UserEntity user = userRepository.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Token user not found"));

        ExpensesEntity newExpenses = new ExpensesEntity();
        newExpenses.setUser(user);
        newExpenses.setDescription(dto.description());
        newExpenses.setAmount(dto.amount());
        newExpenses.setExpenseDate(dto.expenseDate());
        newExpenses.setCategory(category);
        log.info("New expenses created");

        return expensesRepository.save(newExpenses);
    }

    public ExpensesEntity updateExpenses(String token, Integer expenseId, ExpenseUpdateDto dto) {
        String email = jwtService.extractEmail(token);
        ExpensesEntity existingExpense = expensesRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found!"));

        if (dto.description() != null) {
            existingExpense.setDescription(dto.description());
        }
        if (dto.amount() != null) {
            existingExpense.setAmount(dto.amount());
        }
        if (dto.expense_date() != null) {
            existingExpense.setExpenseDate(dto.expense_date());
        }
        if (dto.expenseId() != null) {
            CategoriesEntity category = categoriesRepository.findById(dto.expenseId())
                    .orElseThrow(() -> new RuntimeException("Category not found!"));
            existingExpense.setCategory(category);
        }
        log.info("Expenses updated :: {}", existingExpense);
        return expensesRepository.save(existingExpense);
    }

    @Transactional
    public void deleteExpenses(String token, Integer expenseId) {
        String email = jwtService.extractEmail(token);

        ExpensesEntity existingExpensense = expensesRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found!"));

        expensesRepository.delete(existingExpensense);
        log.info("Expenses deleted ID :: {}", existingExpensense.getId());
    }

    //TODO: //Criar a classe pastweekExpenses para buscar as despesas da semana passada

    public List<ExpensesEntity> pastWeekExpenses(String token) {
        String email = jwtService.extractEmail(token);
        UserEntity user = userRepository.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Token user not found"));

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusWeeks(1);

        List<ExpensesEntity> expenses = expensesRepository.findByUserAndExpenseDateBetween(user, startDate, endDate);
        log.info("Past week expenses fetched for user {} :: count={}", email, expenses.size());
        return expenses;
    }

    public List<ExpensesEntity> pastMonthExpenses(String token) {
        String email = jwtService.extractEmail(token);
        UserEntity user = userRepository.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Token user not found"));

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);

        List<ExpensesEntity> expenses = expensesRepository.findByUserAndExpenseDateBetween(user, startDate, endDate);
        log.info("Past month expenses fetched for user {} :: count={}", email, expenses.size());
        return expenses;
    }

    public List<ExpensesEntity> lastThreeMonthsExpenses(String token) {
        String email = jwtService.extractEmail(token);
        UserEntity user = userRepository.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Token user not found"));

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(3);

        List<ExpensesEntity> expenses = expensesRepository.findByUserAndExpenseDateBetween(user, startDate, endDate);
        log.info("Last 3 months expenses fetched for user {} :: count={}", email, expenses.size());
        return expenses;
    }
}
