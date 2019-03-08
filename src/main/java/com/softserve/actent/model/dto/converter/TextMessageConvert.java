package com.softserve.actent.model.dto.converter;

import com.softserve.actent.model.dto.message.CreateTextMessageDto;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.service.ChatService;
import com.softserve.actent.service.UserService;
import com.softserve.actent.utils.modelmapper.IModelMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextMessageConvert implements IModelMapperConverter<Message, CreateTextMessageDto> {

    private  final
    UserService userService;

    private final
    ModelMapper modelMapper;

    private final
    ChatService chatService;

    @Autowired
    public TextMessageConvert(ModelMapper modelMapper, UserService userService, ChatService chatService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Override
    public Message convertToEntity(CreateTextMessageDto dto) {
        Message message = modelMapper.map(dto, Message.class);
        message.setSender(userService.get(dto.getSenderId()));
        message.setChat(chatService.getChatById(dto.getChatId()));
        return message;
    }

    @Override
    public CreateTextMessageDto convertToDto(Message entity) {
        return modelMapper.map(entity, CreateTextMessageDto.class);
    }
}
