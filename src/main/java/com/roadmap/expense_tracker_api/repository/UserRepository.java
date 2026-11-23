package com.roadmap.expense_tracker_api.repository;

import com.roadmap.expense_tracker_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmailUser(String email);
}
