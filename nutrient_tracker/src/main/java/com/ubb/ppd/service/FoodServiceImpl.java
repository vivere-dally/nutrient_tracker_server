package com.ubb.ppd.service;

import com.ubb.ppd.domain.exception.FoodNotFoundException;
import com.ubb.ppd.domain.model.Food;
import com.ubb.ppd.domain.model.dto.FoodDTO;
import com.ubb.ppd.repository.FoodRepository;
import com.ubb.ppd.repository.NutrientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final NutrientRepository nutrientRepository;

    public FoodServiceImpl(FoodRepository foodRepository, NutrientRepository nutrientRepository) {
        this.foodRepository = foodRepository;
        this.nutrientRepository = nutrientRepository;
    }

    @Override
    public FoodDTO saveFood(FoodDTO foodDTO) {
        Food food = foodDTO.toEntity();
        food.setNutrients(foodDTO.getNutrientIds()
                .stream()
                .map(this.nutrientRepository::getOne)
                .collect(Collectors.toList())
        );

        return new FoodDTO(this.foodRepository.save(food));
    }

    @Override
    public FoodDTO updateFood(FoodDTO foodDTO) {
        if (this.foodRepository.findById(foodDTO.getId()).isEmpty()) {
            throw new FoodNotFoundException("The food with the given ID was not found!");
        }

        Food food = foodDTO.toEntity();
        food.setNutrients(foodDTO.getNutrientIds()
                .stream()
                .map(this.nutrientRepository::getOne)
                .collect(Collectors.toList())
        );

        return new FoodDTO(foodRepository.save(food));
    }

    @Override
    public FoodDTO deleteFood(FoodDTO foodDTO) {
        if (this.foodRepository.findById(foodDTO.getId()).isEmpty()) {
            throw new FoodNotFoundException("The food with the given ID was not found!");
        }

        this.foodRepository.deleteById(foodDTO.getId());
        return foodDTO;
    }

    @Override
    public FoodDTO getFoodById(Long id) {
        Optional<Food> foodOptional = this.foodRepository.findById(id);
        
        return foodOptional
                .map(FoodDTO::new)
                .orElseThrow(() -> new FoodNotFoundException("The food with the given ID was not found!"));
    }

    @Override
    public List<FoodDTO> getFoods() {
        return this.foodRepository.findAll()
                .stream()
                .map(FoodDTO::new)
                .collect(Collectors.toList());
    }
}
