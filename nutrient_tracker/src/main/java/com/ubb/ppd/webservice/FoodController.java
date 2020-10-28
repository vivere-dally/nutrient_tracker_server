package com.ubb.ppd.webservice;

import com.ubb.ppd.domain.model.dto.FoodDTO;
import com.ubb.ppd.service.FoodService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "/nutrient", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequestMapping("/food")
@RestController
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the food by the specified id", response = FoodDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/{id}")
    public FoodDTO getFoodById(
            @ApiParam(name = "id", type = "long", value = "ID of the food", example = "1")
            @PathVariable Long id
    ) {
        log.debug("Entered class = FoodService & method = getFoodById");
        return this.foodService.getFoodById(id);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the foods", response = FoodDTO.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public List<FoodDTO> getFoods() {
        log.debug("Entered class = FoodService & method = getFoods");
        return this.foodService.getFoods();
    }
}
