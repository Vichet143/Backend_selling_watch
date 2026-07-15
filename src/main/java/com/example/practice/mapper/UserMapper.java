package com.example.practice.mapper;

import com.example.practice.config.security.AuthUser;
import com.example.practice.dto.LoginResponseDTO;
import com.example.practice.dto.UserDTO;
import com.example.practice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDto (User user);
    LoginResponseDTO toResponseLogin (AuthUser authUser);

    @Mapping(source = "firstname", target = "firstName")
    @Mapping(source = "lastname", target = "lastName")
    @Mapping(source = "username", target = "userName")
    UserDTO toUserDtoResponse(AuthUser authUser);
}
