package com.github.maxomys.webstore.api.mappers;

import com.github.maxomys.webstore.api.dtos.UserDto;
import com.github.maxomys.webstore.domain.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto userToUserDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .productIds(user.getProducts().stream()
                .map(product -> product.getId())
                .collect(Collectors.toList()))
            .build();
    }

    public User userDtoToUser(UserDto dto) {
        User user = new User();

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());

        return user;
    }

}
