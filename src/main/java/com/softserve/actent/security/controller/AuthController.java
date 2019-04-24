package com.softserve.actent.security.controller;

import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.entity.AuthProvider;
import com.softserve.actent.model.entity.Role;
import com.softserve.actent.model.entity.Status;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.notification.EmailNotification;
import com.softserve.actent.security.JwtTokenProvider;
import com.softserve.actent.security.model.dto.request.SignInDto;
import com.softserve.actent.security.model.dto.request.SignUpDto;
import com.softserve.actent.security.model.dto.response.JwtAuthResponseDto;
import com.softserve.actent.service.UserService;
import com.softserve.actent.verification.service.SendEmail;
import com.softserve.actent.verification.service.Verification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.softserve.actent.constant.UrlConstants.API_V1;
import static com.softserve.actent.constant.UrlConstants.AUTH;

@RestController
@RequestMapping(API_V1 + AUTH)
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private SendEmail sendEmail;

    @Autowired
    private Verification verification;

    @Autowired
    private EmailNotification emailNotification;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthResponseDto authenticateUser(@Validated @RequestBody SignInDto signInDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInDto.getUsernameOrEmail(), signInDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = tokenProvider.generateToken(authentication);
        return new JwtAuthResponseDto(jwtToken);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto registerUser(@Validated @RequestBody SignUpDto signUpDto) {

        User user = modelMapper.map(signUpDto, User.class);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = Role.ROLE_USER;

        user.setRoleset(Collections.singleton(userRole));

        user.setStatus(Status.NON_VERIFIED);
        user.setUuid(verification.createUuidWithSalt());
        user = userService.add(user);
        user.setAuthProvider(AuthProvider.local);
//        sendEmail.sendSimpleEmail(user.getEmail(), user);

        String confirmUrl = "http://localhost:3000/confirm?login=" + user.getLogin() + "&uuid=" + user.getUuid();
        String content = "To verify your account and have all advantages of our service, " +
                "you need to click on the link below. \n \n" + confirmUrl + "\n\nWish you success!\nSincerely yourth, Actent's team";
        String subject = "Verification mail";
        emailNotification.sendEmail(user.getEmail(), subject, content);

        return new IdDto(user.getId());
    }
}
