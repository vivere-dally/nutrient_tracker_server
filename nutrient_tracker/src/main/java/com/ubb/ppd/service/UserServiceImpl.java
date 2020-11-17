package com.ubb.ppd.service;

import com.ubb.ppd.domain.exception.UniqueUsernameException;
import com.ubb.ppd.domain.exception.UserNotFoundException;
import com.ubb.ppd.domain.model.SecurityUser;
import com.ubb.ppd.domain.model.User;
import com.ubb.ppd.domain.model.dto.UserDTO;
import com.ubb.ppd.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        try {
            return new UserDTO(this.userRepository.save(userDTO.toEntity()));
        } catch (DataIntegrityViolationException exception) {
            throw new UniqueUsernameException("This username already exists.");
        }
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .map(UserDTO::new)
                .orElseThrow(() -> new UserNotFoundException("Username was not found!"));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = this.userRepository.findByUsername(s);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Username was not found!"));
        return new SecurityUser(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
