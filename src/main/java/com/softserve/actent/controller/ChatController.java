package com.softserve.actent.controller;

import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.chat.AddChatDto;
import com.softserve.actent.model.dto.chat.ChatInfoDto;
import com.softserve.actent.model.dto.chat.UserForChatDto;
import com.softserve.actent.model.dto.converter.ChatInfoConverter;
import com.softserve.actent.model.entity.Chat;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.service.ChatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(UrlConstants.API_V1)
public class ChatController {

    private final ChatService chatService;
    private final ChatInfoConverter chatInfoConverter;
    private final ModelMapper modelMapper;

    @Autowired
    public ChatController(ChatService chatService, ChatInfoConverter chatInfoConverter, ModelMapper modelMapper) {
        this.chatService = chatService;
        this.chatInfoConverter = chatInfoConverter;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/chats")
    @PreAuthorize("hasRole=('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addEventChat(@Validated @RequestBody AddChatDto addChatDto) {
        Chat chat = chatService.addChat(addChatDto.getChatType());
        return new IdDto(chat.getId());
    }

    @DeleteMapping(value = "/chats/{chatId}")
    @PreAuthorize("hasRole=('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventChatById(@PathVariable(value = "chatId") @NotNull @Positive Long chatId){
        chatService.deleteChatById(chatId);
    }

    @PutMapping(value = "/chats/{chatId}/bannedUsers/{userId}")
    @PreAuthorize("hasRole=('USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserForChatDto> banUserByChatAndUserId(@PathVariable(value = "chatId") @NotNull @Positive Long chatId,
                                                       @PathVariable(value = "userId") @NotNull @Positive Long userId){

        Chat chat = chatService.banUserInChat(chatId, userId);
        List<User> bannedUsers = chat.getBannedUsers();
        return bannedUsers.stream()
                .map(user -> modelMapper.map(user, UserForChatDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/chats/{chatId}/info")
    @PreAuthorize("hasRole=('USER')")
    @ResponseStatus(HttpStatus.OK)
    public ChatInfoDto getChatInfo(@PathVariable(value = "chatId") @NotNull @Positive Long chatId){
        ChatInfoDto chatInfoDto = chatInfoConverter.convertToDto(chatService.getChatById(chatId));
        return chatInfoDto;
    }

    @PutMapping(value = "/chats/{chatId}/unban/{userId}")
    @PreAuthorize("hasRole=('USER')")
    @ResponseStatus(HttpStatus.OK)
    public ChatInfoDto unBannedUserByChatId(@PathVariable(value = "chatId") @Positive @NotNull Long chatId,
                                            @PathVariable(value = "userId") @Positive @NotNull Long userId){

        ChatInfoDto chatInfoDto = chatInfoConverter.convertToDto(chatService.unBanUserFromChat(chatId, userId));
        return chatInfoDto;
    }
}
