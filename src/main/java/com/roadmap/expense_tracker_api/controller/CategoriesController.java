package com.roadmap.expense_tracker_api.controller;

import com.roadmap.expense_tracker_api.entity.CategoriesEntity;
import com.roadmap.expense_tracker_api.service.CategoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriesEntity>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.findAll());
    }
}
