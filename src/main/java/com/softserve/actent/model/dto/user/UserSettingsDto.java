package com.softserve.actent.model.dto.user;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.model.entity.Role;
import com.softserve.actent.model.entity.Sex;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
public class UserSettingsDto {

    @NotBlank(message = StringConstants.EMPTY_USER_LOGIN)
    @Size(max = NumberConstants.USER_LOGIN_MAX_LENGTH, min = NumberConstants.USER_LOGIN_MIN_LENGTH, message = StringConstants.USER_LOGIN_LENGTH_RANGE)
    private String login;

//    @NotBlank(message = StringConstants.EMPTY_USER_EMAIL)
    @Size(max = NumberConstants.USER_EMAIL_MAX_LENGTH, message = StringConstants.USER_EMAIL_LENGTH_RANGE)
    @Email(message = StringConstants.EMAIL_NOT_VALID)
    private String email;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number not valid")
    private String phone;

    @NotBlank(message = StringConstants.EMPTY_USER_FIRST_NAME)
    @Size(max = NumberConstants.USER_FIRST_NAME_MAX_LENGTH, min = NumberConstants.USER_FIRST_NAME_MIN_LENGTH, message = StringConstants.USER_FIRST_NAME_LENGTH_RANGE)
    private String firstName;

    @NotBlank(message = StringConstants.EMPTY_USER_LAST_NAME)
    @Size(max = NumberConstants.USER_LAST_NAME_MAX_LENGTH, min = NumberConstants.USER_LAST_NAME_MIN_LENGTH, message = StringConstants.USER_LAST_NAME_LENGTH_RANGE)
    private String lastName;

//    @NotBlank(message = StringConstants.EMPTY_USER_PASSWORD)
//    @Size(max = NumberConstants.USER_PASSWORD_MAX_LENGTH, min = NumberConstants.USER_PASSWORD_MIN_LENGTH, message = StringConstants.USER_PASSWORD_LENGTH_RANGE)
//    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = StringConstants.BIRTHDAY_CANNOT_BE_FUTURE_DATE)
    private LocalDate birthDate;

    @Positive(message = StringConstants.USER_AVATAR_ID_SHOULD_BE_GREATER_THAN_ZERO)
    private Long avatarId;

    private UserLocationDto location;

    @Size(max = NumberConstants.USER_BIO_MAX_LENGTH, message = StringConstants.BIO_TOO_LONG)
    private String bio;

    private Sex sex;

    private Role role;
}
