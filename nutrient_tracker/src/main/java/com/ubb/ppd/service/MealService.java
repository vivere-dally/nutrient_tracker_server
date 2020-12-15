package com.ubb.ppd.service;

import com.ubb.ppd.domain.model.dto.MealDTO;

import java.util.List;

public interface MealService {
    //region CRUD
    MealDTO saveMeal(MealDTO mealDTO, long userId);

    MealDTO updateMeal(MealDTO mealDTO, long userId);

    MealDTO deleteMeal(MealDTO mealDTO, long userId);

    MealDTO getMealById(long id, long userId);
    //endregion

    List<MealDTO> getMealsByUserId(Integer page, Integer size, String sortBy, String byComment, Boolean isEaten, long userId);
}
