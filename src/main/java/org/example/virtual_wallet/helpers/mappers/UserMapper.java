package org.example.virtual_wallet.helpers.mappers;

import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.UserDto;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User dtoUserCreate(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setPassword(userDto.getPasswordConfirm());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPicture(userDto.getPhotoUrl());
        return user;
    }
    public User dtoUserUpdate(UserDto userDto) {
        User user = new User();
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setPicture(userDto.getPhotoUrl());
        return user;
    }

    public User updateUser(int id, UserDto userDto){
        User user = userService.getById(id);
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPicture(userDto.getPhotoUrl());
        return user;
    }
}
