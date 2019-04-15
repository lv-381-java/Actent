package com.softserve.actent.controller;

import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.SubscribeDto;
import com.softserve.actent.model.dto.message.CreateImageMessageDto;
import com.softserve.actent.model.dto.message.ViewMessageDto;
import com.softserve.actent.model.entity.City;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.model.entity.Subscribe;
import com.softserve.actent.repository.CityRepository;
import com.softserve.actent.security.annotation.CurrentUser;
import com.softserve.actent.security.model.UserPrincipal;
import com.softserve.actent.service.CityService;
import com.softserve.actent.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class SubscribeController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CityRepository cityRepository;


    @Autowired
    public SubscribeController(ModelMapper modelMapper, UserService userService, CityRepository cityRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.cityRepository = cityRepository;
    }

    @PostMapping(value = "/subscribers")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addImage(@Validated @RequestBody SubscribeDto subscribeDto,
                          @ApiIgnore @CurrentUser UserPrincipal currentUser) {
        Subscribe subscribe = modelMapper.map(subscribeDto, Subscribe.class);
        subscribe.setSubscriber(userService.get(currentUser.getId()));
        subscribe.setCity(cityRepository.findByName(subscribeDto.getCity()));

        return new IdDto(subscribe.getId());
    }

}
