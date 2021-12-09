package com.github.maxomys.webstore.services;

import com.github.maxomys.webstore.api.dtos.UserDto;
import com.github.maxomys.webstore.api.mappers.UserMapper;
import com.github.maxomys.webstore.auth.UserService;
import com.github.maxomys.webstore.domain.User;
import com.github.maxomys.webstore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    UserMapper userMapper;
    UserService userService;

    @BeforeEach
    void setup() {
        passwordEncoder = new BCryptPasswordEncoder(10);
        userMapper = new UserMapper();
        userService = new UserService(userRepository, passwordEncoder, userMapper);
    }

    @Test
    void createNewUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("user1");
        userDto.setPassword("password");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenReturn(userMapper.userDtoToUser(userDto));

        UserDto savedUserDto = userService.createNewUser(userDto);

        verify(userRepository).save(userArgumentCaptor.capture());
        assertNotNull(userArgumentCaptor.getValue().getPassword());
        assertNotEquals("password", userArgumentCaptor.getValue().getPassword());

        assertEquals("user1", savedUserDto.getUsername());
    }

}