package com.ubb.ppd.service;

import com.ubb.ppd.domain.model.dto.UserDTO;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);

    UserDTO getUserByUsername(String username);
}
