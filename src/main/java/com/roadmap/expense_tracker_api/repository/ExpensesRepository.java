package com.roadmap.expense_tracker_api.repository;

import com.roadmap.expense_tracker_api.entity.ExpensesEntity;
import com.roadmap.expense_tracker_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpensesRepository extends JpaRepository<ExpensesEntity, Integer> {
    //List<ExpensesEntity> findByUserEntity_Id(Integer id);
}
