package com.roadmap.expense_tracker_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String CategoryName;
}
