package com.softserve.actent.controller;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.converter.ViewMessageConverter;
import com.softserve.actent.model.dto.message.CreateImageMessageDto;
import com.softserve.actent.model.dto.message.CreateTextMessageDto;
import com.softserve.actent.model.dto.message.ViewMessageDto;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.security.annotation.CurrentUser;
import com.softserve.actent.security.model.UserPrincipal;
import com.softserve.actent.service.ChatService;
import com.softserve.actent.service.MessageService;
import com.softserve.actent.service.UserService;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping(UrlConstants.API_V1)
public class MessageController {

    private final MessageService messageService;

    private final UserService userService;

    private final ChatService chatService;

    private final ViewMessageConverter viewMessageConverter;

    private final ModelMapper modelMapper;

    @Autowired
    public MessageController(MessageService messageService,
                             ViewMessageConverter viewMessageConverter,
                             ModelMapper modelMapper,
                             UserService userService,
                             ChatService chatService) {

        this.messageService = messageService;
        this.viewMessageConverter = viewMessageConverter;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.chatService = chatService;
    }


    @PostMapping(value = "/textMessages")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ViewMessageDto addMessage(@Validated @RequestBody CreateTextMessageDto createMessageDto,
                                     @ApiIgnore @CurrentUser UserPrincipal currentUser) {

        Message message = modelMapper.map(createMessageDto, Message.class);
        message.setSender(userService.get(currentUser.getId()));
        message.setChat(chatService.getChatById(createMessageDto.getChatId()));

        return viewMessageConverter.convertToDto(messageService.add(message));
    }

    @PostMapping(value = "/imageMessages")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ViewMessageDto addImage(@Validated @RequestBody CreateImageMessageDto createImageMessageDto,
                                   @ApiIgnore @CurrentUser UserPrincipal currentUser) {
        Message message = modelMapper.map(createImageMessageDto, Message.class);
        message.setSender(userService.get(currentUser.getId()));
        message.setChat(chatService.getChatById(createImageMessageDto.getChatId()));

        return viewMessageConverter.convertToDto(messageService.addImageMessage(message));
    }

    @GetMapping(value = "/currentMessages/{id}/{page}/{size}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewMessageDto> getCurrentMessagesByChatId(@PathVariable @NotNull(message = StringConstants.CHAT_ID_SHOULD_NOT_BE_NULL)
                                                           @Positive(message = StringConstants.CHAT_ID_SHOULD_BE_POSITIVE) Long id,
                                                           @PathVariable(value = "page") @Min(value = 0) int page,
                                                           @PathVariable(value = "size") @Positive @NonNull int size) {

        return viewMessageConverter.convertToDto(messageService.getCurrentMessagesByChatId(id, PageRequest.of(page, size)));

    }

    @GetMapping(value = "/messages/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewMessageDto> getMessagesByChatId(@PathVariable @NotNull(message = StringConstants.CHAT_ID_SHOULD_NOT_BE_NULL)
                                                    @Positive(message = StringConstants.CHAT_ID_SHOULD_BE_POSITIVE) Long id) {

        return viewMessageConverter.convertToDto(messageService.getMessagesByChatId(id));
    }

    @PutMapping(value = "/messages/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public ViewMessageDto updateMessage(@PathVariable @NotNull
                                        @Positive(message = StringConstants.MESSAGE_ID_SHOULD_BE_POSITIVE) Long id,
                                        @Validated @RequestBody CreateTextMessageDto createMessageDto,
                                        @ApiIgnore @CurrentUser UserPrincipal currentUser) {
        Message message = modelMapper.map(createMessageDto, Message.class);
        message.setSender(userService.get(currentUser.getId()));
        Message message1 = messageService.update(message, id);
        message.setChat(chatService.getChatById(createMessageDto.getChatId()));
        return viewMessageConverter.convertToDto(message1);
    }

    @DeleteMapping(value = "/textMessages/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessageById(@PathVariable @NotNull
                                  @Positive(message = StringConstants.MESSAGE_ID_SHOULD_BE_POSITIVE) Long id) {

        messageService.delete(id);

    }

}
