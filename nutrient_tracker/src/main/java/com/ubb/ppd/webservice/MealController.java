package com.ubb.ppd.webservice;

import com.ubb.ppd.domain.model.dto.MealDTO;
import com.ubb.ppd.domain.model.notification.Action;
import com.ubb.ppd.service.MealService;
import com.ubb.ppd.webnotifications.SocketHandler;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@PreAuthorize("isAuthenticated()")
@RequestMapping("/user/{userId}")
@RestController
public class MealController {
    private final SocketHandler socketHandler;
    private final MealService mealService;

    public MealController(SocketHandler socketHandler, MealService mealService) {
        this.socketHandler = socketHandler;
        this.mealService = mealService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meal by the specified id", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/meal/{mealId}")
    public MealDTO getMealById(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "mealId", type = "long", value = "ID of the Meal", example = "-1")
            @PathVariable Long mealId
    ) {
        log.debug("Entered class = MealService & method = getNutrientById");
        return this.mealService.getMealById(mealId, userId);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meals", response = MealDTO.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/meal")
    public List<MealDTO> getMeals(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId
    ) {
        log.debug("Entered class = MealService & method = getMeals");
        return this.mealService.getMealsByUserId(userId);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meal with the actual id from the database", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/meal")
    public MealDTO saveMeal(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "mealDTO", type = "MealDTO")
            @RequestBody MealDTO mealDTO
    ) throws Exception {
        log.debug("Entered class = MealService & method = saveMeal");
        var result = this.mealService.saveMeal(mealDTO, userId);
        socketHandler.notifySessions(result, Action.SAVE, userId);
        return result;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the updated meal", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping("/meal/{mealId}")
    public MealDTO updateMeal(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "mealId", type = "long", value = "ID of the meal", example = "-1")
            @PathVariable Long mealId,
            @ApiParam(name = "mealDTO", type = "MealDTO")
            @RequestBody MealDTO mealDTO
    ) throws Exception {
        log.debug("Entered class = MealService & method = updateMeal");
        mealDTO.setId(mealId);
        var result = this.mealService.updateMeal(mealDTO, userId);
        socketHandler.notifySessions(result, Action.UPDATE, userId);
        return result;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the deleted meal", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("/meal/{mealId}")
    public MealDTO deleteMeal(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "mealId", type = "long", value = "ID of the meal", example = "-1")
            @PathVariable Long mealId
    ) throws Exception {
        log.debug("Entered class = MealService & method = deleteMeal");
        var result = this.mealService.deleteMeal(this.mealService.getMealById(mealId, userId), userId);
        socketHandler.notifySessions(result, Action.DELETE, userId);
        return result;
    }
}
