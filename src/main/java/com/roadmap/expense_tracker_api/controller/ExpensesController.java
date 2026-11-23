package com.roadmap.expense_tracker_api.controller;

import com.roadmap.expense_tracker_api.dto.ExpenseUpdateDto;
import com.roadmap.expense_tracker_api.dto.ExpensesDeleteDto;
import com.roadmap.expense_tracker_api.dto.ExpensesDto;
import com.roadmap.expense_tracker_api.entity.ExpensesEntity;
import com.roadmap.expense_tracker_api.service.ExpensesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("expenses")
public class ExpensesController {

    ExpensesService expensesService;

    public ExpensesController(ExpensesService expensesService) {this.expensesService = expensesService;}

    @PostMapping("save-expenses")
    public ResponseEntity<ExpensesEntity> save(@RequestHeader("Authorization")
                                                   String authHeader,
                                               @RequestBody ExpensesDto expensesDto)
    {
        String token = authHeader.replace("Bearer ", "");
        ExpensesEntity savedExpense = expensesService.createExpenses(token, expensesDto);
        return ResponseEntity.ok(savedExpense);
    }

    @PutMapping("update-expense/{id}")
    public ResponseEntity<ExpensesEntity> updateExpenses(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Integer expenseId,
            @RequestBody ExpenseUpdateDto dto)
    {
        String token = authHeader.replace("Bearer ", "");

        ExpensesEntity updatedExpense = expensesService.updateExpenses(token, expenseId, dto);
        return ResponseEntity.ok(updatedExpense);
    }

    @PutMapping("delete-expense/{id}")
    public ResponseEntity<ExpensesEntity> deleteExpenses(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Integer expenseId)
    {
        String token = authHeader.replace("Bearer ", "");

        expensesService.deleteExpenses(token, expenseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("pastweek-expense")
    public ResponseEntity<List<ExpensesEntity>> pastweekExpenses(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        List<ExpensesEntity> expenses = expensesService.pastWeekExpenses(token);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("pastMonth-expense")
    public ResponseEntity<List<ExpensesEntity>> pastMonthExpenses(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        List<ExpensesEntity> expenses = expensesService.pastMonthExpenses(token);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("lastThreeMonths-expense")
    public ResponseEntity<List<ExpensesEntity>> lastThreeMonthsExpenses(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        List<ExpensesEntity> expenses = expensesService.lastThreeMonthsExpenses(token);
        return ResponseEntity.ok(expenses);
    }
}
