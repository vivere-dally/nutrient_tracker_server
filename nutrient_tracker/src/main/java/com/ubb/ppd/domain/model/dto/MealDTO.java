package com.ubb.ppd.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.ubb.ppd.domain.model.Meal;
import com.ubb.ppd.utils.MD5Hash;
import com.ubb.ppd.utils.View;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO implements DTO<Meal, Long> {
    @ApiModelProperty(required = true, example = "-1", value = "id")
    @JsonView(View.Public.class)
    private long id;

    @ApiModelProperty(required = true, example = "tasty", value = "comment")
    @JsonView(View.Public.class)
    private String comment;

    @ApiModelProperty(example = "1994-11-05T13:15:30Z", value = "date")
    @JsonView(View.Public.class)
    private LocalDateTime date;

    @ApiModelProperty(example = "1605294438", value = "dateEpoch")
    @JsonView(View.Public.class)
    private long dateEpoch;

    @ApiModelProperty(required = true, example = "banana,apple", value = "foods")
    @JsonView(View.Public.class)
    private String foods;

    @ApiModelProperty(example = "false", value = "eaten")
    @JsonView(View.Public.class)
    private boolean eaten;

    @ApiModelProperty(example = "6.9", value = "price")
    @JsonView(View.Public.class)
    private float price;

//    @ApiModelProperty(example = "base64String", value = "photo")
//    @JsonView(View.Public.class)
//    private String photo;

    @ApiModelProperty(example = "69.6969696969", value = "latitude")
    @JsonView(View.Public.class)
    private double latitude;

    @ApiModelProperty(example = "42.0420420420", value = "longitude")
    @JsonView(View.Public.class)
    private double longitude;

    @ApiModelProperty(example = "1", value = "userId")
    @JsonView(View.Public.class)
    private long userId;

    @ApiModelProperty(example = "1", value = "version")
    @JsonView(View.Internal.class)
    private long version;

    @ApiModelProperty(example = "1", value = "etag")
    @JsonView(View.Internal.class)
    private String etag;

    public MealDTO(Meal meal) {
        this.id = meal.getId();
        this.comment = meal.getComment();
        this.date = meal.getDate();
        this.dateEpoch = meal.getDate().toEpochSecond(OffsetDateTime.now().getOffset());
        this.foods = meal.getFoods();
        this.eaten = meal.isEaten();
        this.price = meal.getPrice();
//        this.photo = meal.getPhoto();
        this.latitude = meal.getLatitude();
        this.longitude = meal.getLongitude();
        this.userId = meal.getUser().getId();
        this.version = meal.getVersion();
        this.etag = MD5Hash.getInstance().getMD5Hash(String.format("(%d)69lmao420(%d)", meal.getId(), meal.getVersion()));
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
        meal.setLatitude(this.latitude);
        meal.setLongitude(this.longitude);
//        meal.setPhoto(this.photo);
        meal.setVersion(this.version);
        return meal;
    }
}
