package com.ubb.ppd.domain.model.dto;

import com.ubb.ppd.domain.model.Food;
import com.ubb.ppd.domain.model.Meal;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO implements DTO<Meal, Long> {
    @ApiModelProperty(required = true, example = "1", value = "id")
    private long id;

    @ApiModelProperty(required = true, example = "tasty", value = "comment")
    private String comment;

    @ApiModelProperty(example = "null", value = "mealDate")
    private LocalDateTime mealDate;

    @ApiModelProperty(required = true, example = "[6, 9]", value = "foodIds")
    private List<Long> foodIds;

    public MealDTO(Meal meal) {
        this.id = meal.getId();
        this.comment = meal.getComment();
        this.mealDate = meal.getMealDate();
        this.foodIds = meal.getFoods()
                .stream()
                .map(Food::getId)
                .collect(Collectors.toList());
    }

    @Override
    public Meal toEntity() {
        Meal meal = new Meal();
        meal.setId(this.id);
        meal.setComment(this.comment);
        meal.setMealDate(this.mealDate);

        return meal;
    }
}
