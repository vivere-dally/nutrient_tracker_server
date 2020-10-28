package com.ubb.ppd.domain.model.dto;

import com.ubb.ppd.domain.model.Nutrient;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutrientDTO implements DTO<Nutrient, Long> {
    @ApiModelProperty(required = true, example = "1", value = "id")
    private long id;

    @ApiModelProperty(required = true, example = "Protein", value = "name")
    private String name;

    @ApiModelProperty(required = true, example = "0.69", value = "value")
    private float value;

    public NutrientDTO(Nutrient nutrient) {
        this.id = nutrient.getId();
        this.name = nutrient.getName();
        this.value = nutrient.getValue();
    }

    @Override
    public Nutrient toEntity() {
        return new Nutrient(this.id, this.name, this.value);
    }
}
