package com.softserve.actent.controller;

import com.softserve.actent.model.dto.RegisterUserDto;
import com.softserve.actent.model.dto.UserDto;
import com.softserve.actent.model.dto.UserSettingsDto;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final ModelMapper modelMapper;

    private final UserService userService;

    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User user = userService.add(registerUserDtoToEntity(registerUserDto));
        UserDto userDto = registerUserEntityToDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<UserDto> saveUserSetting(@RequestBody UserSettingsDto userSettingsDto, @PathVariable Long id) {
        User user = userService.update(userSettingsToEntity(userSettingsDto), id);
        UserDto userDto = userSettingsEntityToDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userSettingsEntityToDto(userService.get(id)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {

        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private User registerUserDtoToEntity(RegisterUserDto registerUserDto) {
        return modelMapper.map(registerUserDto, User.class);
    }

    private User userSettingsToEntity(UserSettingsDto userSettingsDto) {
        return modelMapper.map(userSettingsDto, User.class);
    }

    private UserDto userSettingsEntityToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    private UserDto registerUserEntityToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }
}
