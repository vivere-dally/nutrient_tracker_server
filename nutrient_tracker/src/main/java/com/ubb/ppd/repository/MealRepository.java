package com.ubb.ppd.repository;

import com.ubb.ppd.domain.model.Meal;
import com.ubb.ppd.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface MealRepository extends JpaRepository<Meal, Long> {
    Page<Meal> findAllByUserAndCommentStartsWithAndEaten(User user, String comment, Boolean eaten, Pageable pageable);

    Page<Meal> findAllByUserAndCommentStartsWith(User user, String comment, Pageable pageable);
}
