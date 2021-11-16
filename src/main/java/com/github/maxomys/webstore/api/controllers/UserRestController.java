package com.github.maxomys.webstore.api.controllers;

import lombok.RequiredArgsConstructor;
import com.github.maxomys.webstore.api.dtos.UserDto;
import com.github.maxomys.webstore.api.mappers.UserMapper;
import com.github.maxomys.webstore.auth.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {
    
    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
            .map(userMapper::userToUserDto)
            .collect(Collectors.toList());   
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userMapper.userToUserDto(userService.getUserById(userId));
    }

}
