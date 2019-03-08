package com.softserve.actent.model.dto.converter;

import com.softserve.actent.model.dto.message.CreateImageMessageDto;
import com.softserve.actent.model.dto.message.CreateTextMessageDto;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.service.ChatService;
import com.softserve.actent.service.UserService;
import com.softserve.actent.utils.modelmapper.IModelMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageMessageConverter implements IModelMapperConverter<Message, CreateImageMessageDto> {

    private  final
    UserService userService;

    private final
    ModelMapper modelMapper;

    private final
    ChatService chatService;

    @Autowired
    public ImageMessageConverter(ModelMapper modelMapper, UserService userService, ChatService chatService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.chatService = chatService;
    }
    @Override
    public Message convertToEntity(CreateImageMessageDto createImageMessageDto) {
        Message message = modelMapper.map(createImageMessageDto, Message.class);
        message.setSender(userService.get(createImageMessageDto.getSenderId()));
        message.setChat(chatService.getChatById(createImageMessageDto.getChatId()));
        return message;
    }

    @Override
    public CreateImageMessageDto convertToDto(Message entity) {
        return modelMapper.map(entity, CreateImageMessageDto.class);
    }
}
