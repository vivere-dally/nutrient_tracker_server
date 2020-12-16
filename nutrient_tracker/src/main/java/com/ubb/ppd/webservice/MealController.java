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

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Api(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@PreAuthorize("isAuthenticated()")
@RequestMapping("/user/{userId}")
@RestController
public class MealController {
    private final SocketHandler socketHandler;
    private final MealService mealService;
    private final MessageDigest messageDigest;

    private String getMD5Hash(String data) {
        byte[] bytes = messageDigest.digest(data.getBytes());
        BigInteger bigInteger = new BigInteger(1, bytes);
        return bigInteger.toString(16);
    }

    public MealController(SocketHandler socketHandler, MealService mealService) throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("MD5");
        this.socketHandler = socketHandler;
        this.mealService = mealService;
    }

    @GetMapping("/meal")
    public List<MealDTO> getFiltered(
            // Pagination
            @ApiParam(name = "page", type = "Integer", value = "Number of the page", example = "2")
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "size", type = "Integer", value = "The size of one page", example = "5")
            @RequestParam(required = false) Integer size,

            // Filters
            @ApiParam(name = "sortBy", type = "String", value = "sort criteria", example = "name.asc,price.desc")
            @RequestParam(required = false) String sortBy,

            @ApiParam(name = "byComment", type = "String", value = "The comment of meal", example = "salty")
            @RequestParam(required = false) String byComment,

            @ApiParam(name = "isEaten", type = "Boolean", value = "is the meal eaten", example = "true")
            @RequestParam(required = false) Boolean isEaten,

            // User
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,

            // Response
            HttpServletResponse response
    ) {
        log.debug("Entered class = MealController & method = getFiltered");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        return this.mealService.getMealsByUserId(page, size, sortBy, byComment, isEaten, userId);
    }

    //region CRUD
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
            @RequestBody MealDTO mealDTO,
            HttpServletResponse response
    ) throws Exception {
        log.debug("Entered class = MealController & method = saveMeal");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        var result = this.mealService.saveMeal(mealDTO, userId);
        socketHandler.notifySessions(result, Action.SAVE, userId);
        return result;
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
            @PathVariable Long mealId,
            HttpServletResponse response
    ) {
        log.debug("Entered class = MealController & method = getMealById");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        return this.mealService.getMealById(mealId, userId);
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
            @RequestBody MealDTO mealDTO,
            @RequestHeader(value = "If-Match", required = false) String etag,
            HttpServletResponse response
    ) throws Exception {
        log.debug("Entered class = MealController & method = updateMeal");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        MealDTO serverEntity = this.mealService.getMealById(mealId, userId);
        String serverEntityETag = "\"0" + getMD5Hash(socketHandler.gson.toJson(serverEntity)) + "\"";
        if (etag != null && !serverEntityETag.equals(etag)) {
             response.setStatus(412);
            return serverEntity;
        }

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
            @PathVariable Long mealId,
            HttpServletResponse response
    ) throws Exception {
        log.debug("Entered class = MealController & method = deleteMeal");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        var result = this.mealService.deleteMeal(this.mealService.getMealById(mealId, userId), userId);
        socketHandler.notifySessions(result, Action.DELETE, userId);
        return result;
    }

    //endregion
}
