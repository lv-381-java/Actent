package com.softserve.actent.controller;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.SubscribeDto;
import com.softserve.actent.model.entity.Subscribe;
import com.softserve.actent.security.annotation.CurrentUser;
import com.softserve.actent.security.model.UserPrincipal;
import com.softserve.actent.service.SubscribeService;
import com.softserve.actent.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UrlConstants.API_V1)
public class SubscribeController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final SubscribeService subscribeService;


    @Autowired
    public SubscribeController(ModelMapper modelMapper, SubscribeService subscribeService, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.subscribeService = subscribeService;
    }

    @PostMapping(value = "/subscribers")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addSubscribe(@Validated @RequestBody SubscribeDto subscribeDto,
                              @ApiIgnore @CurrentUser UserPrincipal currentUser) {
        System.out.println(subscribeDto);
        Subscribe subscribe = modelMapper.map(subscribeDto, Subscribe.class);
        subscribe.setSubscriber(userService.get(currentUser.getId()));

        subscribe = subscribeService.add(subscribe);
        return new IdDto(subscribe.getId());
    }

    @GetMapping(value = "/subscribers/check")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void checkSubscribers() {
        subscribeService.checkSubscribers();

    }

    @GetMapping(value = "/getSubscriptions")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<SubscribeDto> getAll(@ApiIgnore @CurrentUser UserPrincipal currentUser) {
        List<Subscribe> subscribeList = subscribeService.getAllByUserId(currentUser.getId());
        return subscribeList.stream().map(subscribe ->
                modelMapper.map(subscribe, SubscribeDto.class)).collect(Collectors.toList());
    }


    @DeleteMapping(value = "/subscribers/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessageById(@PathVariable @NotNull
                                  @Positive(message = StringConstants.MESSAGE_ID_SHOULD_BE_POSITIVE) Long id) {
        subscribeService.delete(id);
    }

}
