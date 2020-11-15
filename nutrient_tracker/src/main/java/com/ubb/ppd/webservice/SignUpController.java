package com.ubb.ppd.webservice;

import com.ubb.ppd.domain.model.dto.UserDTO;
import com.ubb.ppd.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

@Api(value = "/signup")
@Slf4j
@RequestMapping("/signup")
@RestController
public class SignUpController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SignUpController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "creates an user account", response = String.class, produces = MediaType.TEXT_HTML_VALUE)
    @PostMapping
    public ResponseEntity<String> signUp(
            @ApiParam(name = "userDTO", type = "UserDTO")
            @RequestBody UserDTO userDTO
    ) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userService.saveUser(userDTO);
        return ResponseEntity.ok("Account created successfully");
    }
}
