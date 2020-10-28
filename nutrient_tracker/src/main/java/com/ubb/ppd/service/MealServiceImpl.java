package com.ubb.ppd.service;

import com.ubb.ppd.domain.exception.MealNotFoundException;
import com.ubb.ppd.domain.model.Meal;
import com.ubb.ppd.domain.model.dto.MealDTO;
import com.ubb.ppd.repository.FoodRepository;
import com.ubb.ppd.repository.MealRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {
    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;

    public MealServiceImpl(FoodRepository foodRepository, MealRepository mealRepository) {
        this.foodRepository = foodRepository;
        this.mealRepository = mealRepository;
    }

    @Override
    public MealDTO saveMeal(MealDTO mealDTO) {
        Meal meal = mealDTO.toEntity();
        meal.setFoods(mealDTO.getFoodIds()
                .stream()
                .map(this.foodRepository::getOne)
                .collect(Collectors.toList())
        );

        return new MealDTO(this.mealRepository.save(meal));
    }

    @Override
    public MealDTO updateMeal(MealDTO mealDTO) {
        if (this.mealRepository.findById(mealDTO.getId()).isEmpty()) {
            throw new MealNotFoundException("The meal with the given ID was not found!");
        }

        Meal meal = mealDTO.toEntity();
        meal.setFoods(mealDTO.getFoodIds()
                .stream()
                .map(this.foodRepository::getOne)
                .collect(Collectors.toList())
        );

        return new MealDTO(this.mealRepository.save(meal));
    }

    @Override
    public MealDTO deleteMeal(MealDTO mealDTO) {
        if (this.mealRepository.findById(mealDTO.getId()).isEmpty()) {
            throw new MealNotFoundException("The meal with the given ID was not found!");
        }

        this.mealRepository.deleteById(mealDTO.getId());
        return mealDTO;
    }

    @Override
    public MealDTO getMealById(Long id) {
        Optional<Meal> mealOptional = this.mealRepository.findById(id);

        return mealOptional
                .map(MealDTO::new)
                .orElseThrow(() -> new MealNotFoundException("The meal with the given ID was not found!"));
    }

    @Override
    public List<MealDTO> getMeals() {
        return this.mealRepository.findAll()
                .stream()
                .map(MealDTO::new)
                .collect(Collectors.toList());
    }
}
