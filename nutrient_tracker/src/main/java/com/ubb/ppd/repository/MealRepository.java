package com.ubb.ppd.repository;

import com.ubb.ppd.domain.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
