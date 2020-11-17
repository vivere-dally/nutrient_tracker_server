package com.ubb.ppd.domain.model.dto;

import com.ubb.ppd.domain.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements DTO<User, Long> {
    @ApiModelProperty(required = true, example = "1", value = "id")
    private long id;

    @ApiModelProperty(required = true, example = "admin", value = "username")
    private String username;

    @ApiModelProperty(required = true, example = "admin", value = "password")
    private String password;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    @Override
    public User toEntity() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
