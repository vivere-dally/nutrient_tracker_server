package com.ubb.ppd.service;

import com.ubb.ppd.domain.exception.MealNotFoundException;
import com.ubb.ppd.domain.exception.UserNotFoundException;
import com.ubb.ppd.domain.model.Meal;
import com.ubb.ppd.domain.model.User;
import com.ubb.ppd.domain.model.dto.MealDTO;
import com.ubb.ppd.repository.MealRepository;
import com.ubb.ppd.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public MealServiceImpl(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    private Meal setUser(MealDTO mealDTO, User user) {
        Meal meal = mealDTO.toEntity();
        meal.setUser(user);
        return meal;
    }

    private List<Sort.Order> getSorting(String sortBy) {
        return Arrays.stream(sortBy.split(","))
                .map(item -> item.split("\\."))
                .map(item -> new Sort.Order(Sort.Direction.fromString(item[1]), item[0]))
                .collect(Collectors.toList());
    }

    @Override
    public MealDTO saveMeal(MealDTO mealDTO, long userId) {
        User user = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with the given ID was not found!"));

        return new MealDTO(this.mealRepository.save(setUser(mealDTO, user)));
    }

    @Override
    public MealDTO updateMeal(MealDTO mealDTO, long userId) {
        User user = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with the given ID was not found!"));

        if (this.mealRepository.findById(mealDTO.getId()).isEmpty()) {
            throw new MealNotFoundException("The meal with the given ID was not found!");
        }

        return new MealDTO(this.mealRepository.save(setUser(mealDTO, user)));
    }

    @Override
    public MealDTO deleteMeal(MealDTO mealDTO, long userId) {
        Meal meal = this.mealRepository
                .findById(mealDTO.getId())
                .orElseThrow(() -> new MealNotFoundException("The meal with the given ID was not found!"));

        if (meal.getUser().getId() != userId) {
            throw new UserNotFoundException("The user with the given ID was not found!");
        }

        this.mealRepository.deleteById(mealDTO.getId());
        return mealDTO;
    }

    @Override
    public MealDTO getMealById(long id, long userId) {
        Meal meal = this.mealRepository
                .findById(id)
                .orElseThrow(() -> new MealNotFoundException("The meal with the given ID was not found!"));

        if (meal.getUser().getId() != userId) {
            throw new UserNotFoundException("The user with the given ID was not found!");
        }

        return new MealDTO(meal);
    }

    @Override
    public List<MealDTO> getMealsByUserId(Integer page, Integer size, String sortBy, String byComment, Boolean isEaten, long userId) {
        Optional<Integer> pageOptional = Optional.ofNullable(page);
        Optional<Integer> sizeOptional = Optional.ofNullable(size);
        Optional<String> sortByOptional = Optional.ofNullable(sortBy);
        if (pageOptional.isEmpty() && sizeOptional.isPresent()) {
            throw new IllegalArgumentException("While page is null size cannot have a value");
        }

        User user = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with the given ID was not found!"));
        if (pageOptional.isPresent() && sizeOptional.isEmpty()) {
            sizeOptional = Optional.of(5);
        }

        Pageable pageable = PageRequest.of(
                pageOptional.orElse(0),
                sizeOptional.orElse(Integer.MAX_VALUE),
                sortByOptional.map(sort -> Sort.by(getSorting(sort))).orElse(Sort.unsorted())
        );

        if (byComment == null) {
            byComment = "";
        }

        List<Meal> result;
        if (isEaten != null) {
            result = this.mealRepository.findAllByUserAndCommentStartsWithAndEaten(user, byComment, isEaten, pageable).getContent();
        }
        else {
            result = this.mealRepository.findAllByUserAndCommentStartsWith(user, byComment, pageable).getContent();
        }

        return result
                .stream()
                .map(MealDTO::new)
                .collect(Collectors.toList());
    }
}
