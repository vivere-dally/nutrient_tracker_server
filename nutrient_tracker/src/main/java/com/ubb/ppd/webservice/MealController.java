package com.ubb.ppd.webservice;

import com.ubb.ppd.domain.model.dto.MealDTO;
import com.ubb.ppd.service.MealService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(value = "/meal", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequestMapping("/meal")
@RestController
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meal by the specified id", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/{id}")
    public MealDTO getMealById(
            @ApiParam(name = "id", type = "long", value = "ID of the meal", example = "1")
            @PathVariable Long id
    ) {
        log.debug("Entered class = MealService & method = getNutrientById");
        return this.mealService.getMealById(id);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meals", response = MealDTO.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public List<MealDTO> getMeals() {
        log.debug("Entered class = MealService & method = getMeals");
        return this.mealService.getMeals();
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meal with the actual id from the database", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping
    public MealDTO saveMeal(
            @ApiParam(name = "mealDTO", type = "MealDTO")
            @RequestBody MealDTO mealDTO
    ) {
        log.debug("Entered class = MealService & method = saveMeal");
        return this.mealService.saveMeal(mealDTO);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the updated meal", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping("/{id}")
    public MealDTO updateMeal(
            @ApiParam(name = "id", type = "long", value = "ID of the meal", example = "1")
            @PathVariable Long id,
            @ApiParam(name = "mealDTO", type = "MealDTO")
            @RequestBody MealDTO mealDTO
    ) {
        log.debug("Entered class = MealService & method = updateMeal");
        mealDTO.setId(id);
        mealDTO.setMealDate(LocalDateTime.now());
        return this.mealService.updateMeal(mealDTO);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the deleted meal", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/{id}")
    public MealDTO deleteMeal(
            @ApiParam(name = "id", type = "long", value = "ID of the meal", example = "1")
            @PathVariable Long id
    ) {
        log.debug("Entered class = MealService & method = deleteMeal");
        return this.mealService.deleteMeal(this.mealService.getMealById(id));
    }
}
