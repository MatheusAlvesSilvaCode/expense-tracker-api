package com.roadmap.expense_tracker_api.service;

import com.roadmap.expense_tracker_api.entity.CategoriesEntity;
import com.roadmap.expense_tracker_api.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<CategoriesEntity> findAll() {
        return categoriesRepository.findAll();
    }
}