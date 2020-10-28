package com.ubb.ppd.webservice;

import com.ubb.ppd.domain.model.dto.NutrientDTO;
import com.ubb.ppd.service.NutrientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "/nutrient", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequestMapping("/nutrient")
@RestController
public class NutrientController {
    private final NutrientService nutrientService;

    public NutrientController(NutrientService nutrientService) {
        this.nutrientService = nutrientService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the nutrient by the specified id", response = NutrientDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/{id}")
    public NutrientDTO getNutrientById(@PathVariable Long id) {
        log.debug("Entered class = NutrientController & method = getNutrientById");
        return this.nutrientService.getNutrientById(id);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the nutrients", response = NutrientDTO.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public List<NutrientDTO> getNutrients() {
        log.debug("Entered class = NutrientController & method = getNutrients");
        return this.nutrientService.getNutrients();
    }
}
