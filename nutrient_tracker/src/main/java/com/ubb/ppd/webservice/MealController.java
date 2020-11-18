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
        log.debug("Entered class = MealController & method = getMealById");
        return this.mealService.getMealById(mealId, userId);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meals", response = MealDTO.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/meal")
    public List<MealDTO> getMeals(
            @ApiParam(name = "page", type = "Integer", value = "Number of the page", example = "2")
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "size", type = "Integer", value = "The size of one page", example = "5")
            @RequestParam(required = false) Integer size,
            @ApiParam(name = "sortBy", type = "String", value = "sort criteria", example = "name.asc,price.desc")
            @RequestParam(required = false) String sortBy,
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId
    ) {
        log.debug("Entered class = MealController & method = getMeals");
        return this.mealService.getMealsByUserId(page, size, sortBy, userId);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meals", response = MealDTO.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/meal/filter")
    public List<MealDTO> getMealsByComment(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "comment", type = "String", value = "The comment of meal", example = "salty")
            @RequestParam(value = "comment") String comment
    ) {
        log.debug("Entered class = MealController & method = getMeals");
        return this.mealService.getMealsByComment(comment, userId);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meals", response = MealDTO.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/meal/eaten")
    public List<MealDTO> getAllEatenMeals(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId
    ) {
        log.debug("Entered class = MealController & method = getAllEatenMeals");
        return this.mealService.getAllEatenMeals(userId);
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
        log.debug("Entered class = MealController & method = saveMeal");
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
        log.debug("Entered class = MealController & method = updateMeal");
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
        log.debug("Entered class = MealController & method = deleteMeal");
        var result = this.mealService.deleteMeal(this.mealService.getMealById(mealId, userId), userId);
        socketHandler.notifySessions(result, Action.DELETE, userId);
        return result;
    }
}
