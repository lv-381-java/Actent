package com.softserve.actent.model.dto.user;

import com.softserve.actent.model.entity.Image;
import com.softserve.actent.model.entity.Role;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {
    private Long id;

    private String login;

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Image avatar;

    private UserLocationDto location;

    private String bio;

    private String sex;

    private Set<Role> roleset;

}
