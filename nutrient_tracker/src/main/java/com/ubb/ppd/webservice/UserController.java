package com.ubb.ppd.webservice;

import com.ubb.ppd.domain.model.dto.UserDTO;
import com.ubb.ppd.service.UserServiceImpl;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@PreAuthorize("isAuthenticated()")
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the user by the specified username", response = UserDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/getByUsername")
    public UserDTO getMealById(
            @ApiParam(name = "username", type = "String", value = "The username of user", example = "admin")
            @RequestParam(value = "username") String username
    ) {
        log.debug("Entered class = UserController & method = getMealById");
        return this.userService.getUserByUsername(username);
    }
}
