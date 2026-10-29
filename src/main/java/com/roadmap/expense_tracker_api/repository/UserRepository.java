package com.roadmap.expense_tracker_api.repository;

import com.roadmap.expense_tracker_api.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
