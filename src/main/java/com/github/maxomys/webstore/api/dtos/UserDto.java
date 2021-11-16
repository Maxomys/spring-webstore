package com.github.maxomys.webstore.api.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    
    private Long id;
    private String username;
    private String password;
    private String email;
    private List<Long> productIds;

}
