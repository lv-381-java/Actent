package com.softserve.actent.controller.messanger;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.model.dto.chat.*;
import com.softserve.actent.model.dto.converter.ViewMessageConverter;
import com.softserve.actent.model.dto.message.CreateImageMessageDto;
import com.softserve.actent.model.dto.message.CreateTextMessageDto;
import com.softserve.actent.model.entity.Image;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.security.annotation.CurrentUser;
import com.softserve.actent.security.model.UserPrincipal;
import com.softserve.actent.service.ChatService;
import com.softserve.actent.service.ImageService;
import com.softserve.actent.service.MessageService;
import com.softserve.actent.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static java.lang.String.format;

@Slf4j
@Controller
public class ChatWebSocketController {

    private final SimpMessageSendingOperations sendingOperations;

    private final MessageService messageService;

    private final ChatService chatService;

    private final UserService userService;

    private final ViewMessageConverter viewMessageConverter;

    private final ImageService imageService;

    private final ModelMapper modelMapper;

    @Autowired
    public ChatWebSocketController(SimpMessageSendingOperations sendingOperations, MessageService messageService,
                                   ChatService chatService, UserService userService, ViewMessageConverter viewMessageConverter, ImageService imageService, ModelMapper modelMapper) {
        this.sendingOperations = sendingOperations;
        this.messageService = messageService;
        this.chatService = chatService;
        this.userService = userService;
        this.viewMessageConverter = viewMessageConverter;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @MessageMapping("/image")
    public void sendImage(@Payload ChatImageMessageDto chatImageMessageDto){
        Image image = new Image();
        image.setFilePath(chatImageMessageDto.getFilePath());
        image = imageService.add(image);

        Message message = new Message();
        message.setImage(image);
        message.setSender(userService.get(chatImageMessageDto.getSenderId()));
        message.setChat(chatService.getChatById(chatImageMessageDto.getChatId()));

        sendingOperations.convertAndSend(format("/topic/messages/%s", chatImageMessageDto.getChatId()),
                viewMessageConverter.convertToDto(messageService.addImageMessage(message)));
    }

    @MessageMapping("/message")
    public void sendMessage(@Payload ChatTextMessageDto chatTextMessageDto){

        Message message = new Message();
        message.setMessageContent(chatTextMessageDto.getMessageContent());
        message.setSender(userService.get(chatTextMessageDto.getSenderId()));
        message.setChat(chatService.getChatById(chatTextMessageDto.getChatId()));

        sendingOperations.convertAndSend(format("/topic/messages/%s", chatTextMessageDto.getChatId()),
                viewMessageConverter.convertToDto(messageService.add(message)));
    }

    @MessageMapping(value = "/message/delete")
    public void deleteMessageById(@Payload DeleteFromDto deleteFromDto) {

        Message message = messageService.get(deleteFromDto.getMessageId());

        DeleteMessageDto deleteMessageDto = new DeleteMessageDto();
        deleteMessageDto.setId(message.getId());
        deleteMessageDto.setSenderId(message.getSender().getId());
        deleteMessageDto.setDelete("TRUE");

        messageService.delete(deleteFromDto.getMessageId());

        sendingOperations.convertAndSend(format("/topic/messages/%s", message.getChat().getId()),
                deleteMessageDto);
    }

    @MessageMapping(value = "/message/update")
    public void updateMessage(@Payload EditFromDto editFromDto){

        Message message = new Message();
        message.setChat(chatService.getChatById(editFromDto.getChatId()));
        message.setMessageContent(editFromDto.getMessageContent());
        message.setSender(userService.get(editFromDto.getSenderId()));

        Message editMessage = messageService.update(message, editFromDto.getMessageId());

        EditMessageDto editMessageDto = new EditMessageDto();
        editMessageDto.setChatId(editMessage.getChat().getId());
        editMessageDto.setMessageContent(editMessage.getMessageContent());
        editMessageDto.setMessageId(editMessage.getId());
        editMessageDto.setSenderId(editMessage.getSender().getId());
        editMessageDto.setUpdate("UPDATE");

        sendingOperations.convertAndSend(format("/topic/messages/%s", editMessage.getChat().getId()), editMessageDto);
    }

}
