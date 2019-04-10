package com.softserve.actent.controller.messanger;

import com.softserve.actent.model.dto.chat.ChatImageMessageDto;
import com.softserve.actent.model.dto.chat.ChatTextMessageDto;
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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;

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

    @Autowired
    public ChatWebSocketController(SimpMessageSendingOperations sendingOperations, MessageService messageService,
                                   ChatService chatService, UserService userService, ViewMessageConverter viewMessageConverter, ImageService imageService) {
        this.sendingOperations = sendingOperations;
        this.messageService = messageService;
        this.chatService = chatService;
        this.userService = userService;
        this.viewMessageConverter = viewMessageConverter;
        this.imageService = imageService;
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

}
