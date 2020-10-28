package com.ubb.ppd.service;

import com.ubb.ppd.domain.model.dto.MealDTO;

import java.util.List;

public interface MealService {
    MealDTO saveMeal(MealDTO mealDTO);

    MealDTO updateMeal(MealDTO mealDTO);

    MealDTO deleteMeal(MealDTO mealDTO);

    MealDTO getMealById(Long id);

    List<MealDTO> getMeals();
}
