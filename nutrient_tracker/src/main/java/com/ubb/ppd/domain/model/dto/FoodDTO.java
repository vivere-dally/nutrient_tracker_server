package com.ubb.ppd.domain.model.dto;

import com.ubb.ppd.domain.model.Food;
import com.ubb.ppd.domain.model.Nutrient;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO implements DTO<Food, Long> {
    @ApiModelProperty(required = true, example = "1", value = "id")
    private long id;

    @ApiModelProperty(required = true, example = "apple", value = "name")
    private String name;

    @ApiModelProperty(required = true, example = "red apple", value = "description")
    private String description;

    @ApiModelProperty(example = "null", value = "image")
    private byte[] image;

    @ApiModelProperty(required = true, example = "[4, 2, 0]", value = "nutrientIds")
    private List<Long> nutrientIds;

    public FoodDTO(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.description = food.getDescription();
        this.image = food.getImage();
        this.nutrientIds = food.getNutrients()
                .stream()
                .map(Nutrient::getId)
                .collect(Collectors.toList());
    }

    @Override
    public Food toEntity() {
        Food food = new Food();
        food.setId(this.id);
        food.setName(this.name);
        food.setDescription(this.description);
        food.setImage(this.image);

        return food;
    }
}
