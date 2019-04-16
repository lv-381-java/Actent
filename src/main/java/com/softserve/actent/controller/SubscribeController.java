package com.softserve.actent.controller;

import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.SubscribeDto;
import com.softserve.actent.model.dto.message.CreateImageMessageDto;
import com.softserve.actent.model.dto.message.ViewMessageDto;
import com.softserve.actent.model.entity.City;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.model.entity.Subscribe;
import com.softserve.actent.repository.CityRepository;
import com.softserve.actent.repository.SubscribeRepository;
import com.softserve.actent.security.annotation.CurrentUser;
import com.softserve.actent.security.model.UserPrincipal;
import com.softserve.actent.service.CityService;
import com.softserve.actent.service.SubscribeService;
import com.softserve.actent.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(UrlConstants.API_V1)
public class SubscribeController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final SubscribeService subscribeService;


    @Autowired
    public SubscribeController(ModelMapper modelMapper, SubscribeService subscribeService, UserService userService, CityRepository cityRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.cityRepository = cityRepository;
        this.subscribeService = subscribeService;
    }

    @PostMapping(value = "/subscribers")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addSubscribe(@Validated @RequestBody SubscribeDto subscribeDto,
                              @ApiIgnore @CurrentUser UserPrincipal currentUser) {
        Subscribe subscribe = modelMapper.map(subscribeDto, Subscribe.class);
        subscribe.setSubscriber(userService.get(currentUser.getId()));

        subscribe = subscribeService.add(subscribe);
        return new IdDto(subscribe.getId());
    }

    @GetMapping(value = "/subscribers/check")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void check() {

        subscribeService.checkSubscribers();

    }

}
