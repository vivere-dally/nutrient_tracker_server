package com.ubb.ppd.webservice;

import com.fasterxml.jackson.annotation.JsonView;
import com.ubb.ppd.domain.model.dto.MealDTO;
import com.ubb.ppd.domain.model.notification.Action;
import com.ubb.ppd.service.MealService;
import com.ubb.ppd.utils.MD5Hash;
import com.ubb.ppd.utils.View;
import com.ubb.ppd.webnotifications.SocketHandler;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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

    @JsonView(View.Public.class)
    @GetMapping("/meal")
    public ResponseEntity<List<MealDTO>> getFiltered(
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

            // Headers
            @RequestHeader(value = "If-None-Match", required = false) String etag,

            // Response
            HttpServletResponse response
    ) {
        log.debug("Entered class = MealController & method = getFiltered");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        List<MealDTO> result = this.mealService.getMealsByUserId(page, size, sortBy, byComment, isEaten, userId);
        String overallEtag = MD5Hash.getInstance().getMD5Hash(
                result.stream().map(MealDTO::getEtag).collect(Collectors.joining(","))
        );
        if (String.format("\"%s\"", overallEtag).equals(etag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity
                .ok()
                .eTag(overallEtag)
                .body(result);
    }

    //region CRUD
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meal with the actual id from the database", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Public.class)
    @PostMapping("/meal")
    public ResponseEntity<MealDTO> saveMeal(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "mealDTO", type = "MealDTO")
            @RequestBody MealDTO mealDTO,

            // Response
            HttpServletResponse response
    ) throws Exception {
        log.debug("Entered class = MealController & method = saveMeal");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        MealDTO result = this.mealService.saveMeal(mealDTO, userId);
        socketHandler.notifySessions(result, Action.SAVE, userId);
        return ResponseEntity
                .ok()
                .eTag(result.getEtag())
                .body(result);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the meal by the specified id", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Public.class)
    @GetMapping("/meal/{mealId}")
    public ResponseEntity<MealDTO> getMealById(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "mealId", type = "long", value = "ID of the Meal", example = "-1")
            @PathVariable Long mealId,

            // Response
            HttpServletResponse response
    ) {
        log.debug("Entered class = MealController & method = getMealById");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        MealDTO result = this.mealService.getMealById(mealId, userId);
        return ResponseEntity
                .ok()
                .eTag(result.getEtag())
                .body(result);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the updated meal", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Public.class)
    @PutMapping("/meal/{mealId}")
    public ResponseEntity<MealDTO> updateMeal(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "mealId", type = "long", value = "ID of the meal", example = "-1")
            @PathVariable Long mealId,
            @ApiParam(name = "mealDTO", type = "MealDTO")
            @RequestBody MealDTO mealDTO,
            @RequestHeader(value = "If-Match", required = false) String etag,

            // Response
            HttpServletResponse response
    ) throws Exception {
        log.debug("Entered class = MealController & method = updateMeal");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        MealDTO serverEntity = this.mealService.getMealById(mealId, userId);
        if (!String.format("\"%s\"", serverEntity.getEtag()).equals(etag)) {
            return ResponseEntity
                    .status(HttpStatus.PRECONDITION_FAILED)
                    .eTag(serverEntity.getEtag())
                    .body(serverEntity);
        }

        mealDTO.setId(mealId);
        mealDTO.setVersion(serverEntity.getVersion()); // MealDTO.version doesn't exist outside the server.
        var result = this.mealService.updateMeal(mealDTO, userId);
        socketHandler.notifySessions(result, Action.UPDATE, userId); // TODO use threadPool to notify
        return ResponseEntity
                .ok()
                .eTag(result.getEtag())
                .body(mealDTO);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the deleted meal", response = MealDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Public.class)
    @DeleteMapping("/meal/{mealId}")
    public ResponseEntity<MealDTO> deleteMeal(
            @ApiParam(name = "userId", type = "long", value = "ID of the User", example = "-1")
            @PathVariable Long userId,
            @ApiParam(name = "mealId", type = "long", value = "ID of the meal", example = "-1")
            @PathVariable Long mealId,

            // Response
            HttpServletResponse response
    ) throws Exception {
        log.debug("Entered class = MealController & method = deleteMeal");
        response.setHeader("Access-Control-Expose-Headers", "ETag");
        MealDTO result = this.mealService.deleteMeal(this.mealService.getMealById(mealId, userId), userId);
        socketHandler.notifySessions(result, Action.DELETE, userId);
        return ResponseEntity
                .ok()
                .eTag(result.getEtag())
                .body(result);
    }

    //endregion
}
