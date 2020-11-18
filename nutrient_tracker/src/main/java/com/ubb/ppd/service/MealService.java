package com.ubb.ppd.service;

import com.ubb.ppd.domain.model.dto.MealDTO;

import java.util.List;

public interface MealService {
    MealDTO saveMeal(MealDTO mealDTO, long userId);

    MealDTO updateMeal(MealDTO mealDTO, long userId);

    MealDTO deleteMeal(MealDTO mealDTO, long userId);

    MealDTO getMealById(long id, long userId);

    List<MealDTO> getMealsByComment(String comment, long userId);

    List<MealDTO> getAllEatenMeals(long userId);

    List<MealDTO> getMealsByUserId(Integer page, Integer size, String sortBy, long userId);
}
