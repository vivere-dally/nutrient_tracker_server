package com.ubb.ppd.domain.model.dto;

import com.ubb.ppd.domain.model.Meal;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO implements DTO<Meal, Long> {
    @ApiModelProperty(required = true, example = "-1", value = "id")
    private long id;

    @ApiModelProperty(required = true, example = "tasty", value = "comment")
    private String comment;

    @ApiModelProperty(example = "1994-11-05T13:15:30Z", value = "date")
    private LocalDateTime date;

    @ApiModelProperty(example = "1605294438", value = "dateEpoch")
    private long dateEpoch;

    @ApiModelProperty(required = true, example = "banana,apple", value = "foods")
    private String foods;

    @ApiModelProperty(example = "false", value = "eaten")
    private boolean eaten;

    @ApiModelProperty(example = "6.9", value = "price")
    private float price;

    @ApiModelProperty(example = "base64String", value = "photo")
    private String photo;

    @ApiModelProperty(example = "69.6969696969", value = "latitude")
    private double latitude;

    @ApiModelProperty(example = "42.0420420420", value = "longitude")
    private double longitude;

    @ApiModelProperty(example = "1", value = "userId")
    private long userId;

    public MealDTO(Meal meal) {
        this.id = meal.getId();
        this.comment = meal.getComment();
        this.date = meal.getDate();
        this.dateEpoch = meal.getDate().toEpochSecond(OffsetDateTime.now().getOffset());
        this.foods = meal.getFoods();
        this.eaten = meal.isEaten();
        this.price = meal.getPrice();
        try {
            this.photo = new String(Base64.getDecoder().decode(new String(meal.getPhoto()).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            this.photo = null;
        }

        this.latitude = meal.getLatitude();
        this.longitude = meal.getLongitude();
        this.userId = meal.getUser().getId();
    }

    @Override
    public Meal toEntity() {
        Meal meal = new Meal();
        meal.setId(this.id);
        meal.setComment(this.comment);
        meal.setDate(this.date);
        meal.setFoods(this.foods);
        meal.setEaten(this.eaten);
        meal.setPrice(this.price);
        byte[] photo;
        try {
            photo = Base64.getEncoder().encode(this.photo.getBytes());
        } catch (Exception e) {
            photo = new byte[0];
        }

        meal.setLatitude(this.latitude);
        meal.setLongitude(this.longitude);
        meal.setPhoto(photo);
        return meal;
    }
}
