package com.softserve.actent.controller;

import com.softserve.actent.model.dto.CreateUserDto;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/users")
    public ResponseEntity<IdDto> addUser(@RequestBody CreateUserDto createUserDto) {

        User user = userService.addUser(new User(createUserDto.getFirstName(), createUserDto.getLastName(),
                createUserDto.getLogin()));

        return new ResponseEntity<>(new IdDto(user.getId()), HttpStatus.CREATED);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() {

        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {

        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}










