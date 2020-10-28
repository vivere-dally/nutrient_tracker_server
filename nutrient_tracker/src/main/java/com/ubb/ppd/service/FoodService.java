package com.ubb.ppd.service;

import com.ubb.ppd.domain.model.dto.FoodDTO;

import java.util.List;

public interface FoodService {
    FoodDTO saveFood(FoodDTO foodDTO);

    FoodDTO updateFood(FoodDTO foodDTO);

    FoodDTO deleteFood(FoodDTO foodDTO);

    FoodDTO getFoodById(Long id);

    List<FoodDTO> getFoods();
}
