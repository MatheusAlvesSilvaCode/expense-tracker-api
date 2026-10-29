package com.roadmap.expense_tracker_api.repository;

import com.roadmap.expense_tracker_api.entity.CategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
}
