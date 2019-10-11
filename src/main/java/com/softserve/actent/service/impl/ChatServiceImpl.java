package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.validation.IncorrectChatTypeException;
import com.softserve.actent.model.entity.Chat;
import com.softserve.actent.model.entity.ChatType;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.ChatRepository;
import com.softserve.actent.repository.MessageRepository;
import com.softserve.actent.service.ChatService;
import com.softserve.actent.service.UserService;
import com.softserve.actent.service.cache.EventCache;
import com.softserve.actent.service.cache.EventCacheMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final EventCache eventCache;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository,
                           UserService userService,
                           MessageRepository messageRepository,
                           EventCache eventCache) {
        this.chatRepository = chatRepository;
        this.userService = userService;
        this.messageRepository = messageRepository;
        this.eventCache = eventCache;
    }

    @Transactional
    @Override
    public Chat addChat(String type) {
        Chat chat = new Chat();

        if(!type.toLowerCase().equals("event") && !type.toLowerCase().equals("direct")){
            throw new IncorrectChatTypeException(ExceptionMessages.ACTIVE_BY_THIS_TYPE_IS_NOT_FOUND,
                    ExceptionCode.INCORRECT_ACTIVITY_TYPE);
        }
        if (type.toLowerCase().equals("event")) {
            chat.setType(ChatType.EVENT);
        }
        if (type.toLowerCase().equals("direct")) {
            chat.setType(ChatType.DIRECT);
        }
        chatRepository.save(chat);
        return chat;
    }

    @Override
    public Chat getChatById(Long chatId) {
        return chatRepository.findById(chatId).
                orElseThrow(() -> new DataNotFoundException(ExceptionMessages.CHAT_BY_THIS_ID_IS_NOT_FOUND,
                        ExceptionCode.CHAT_NOT_FOUND));
    }

    @Transactional
    @Override
    public void deleteChatById(Long chatId) {

        if (chatRepository.existsById(chatId)) {
            eventCache.cacheRefresh(chatId, EventCacheMethod.CHAT);
            chatRepository.deleteById(chatId);
        } else {
            throw new DataNotFoundException(ExceptionMessages.CHAT_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.CHAT_NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public Chat updateChat(Chat chat, Long chatId) {
        if (chatRepository.existsById(chatId)) {
            chat.setId(chatId);
            eventCache.cacheRefresh(chatId, EventCacheMethod.CHAT);
            return chatRepository.save(chat);
        } else {
            throw new DataNotFoundException(ExceptionMessages.CHAT_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.CHAT_NOT_FOUND);
        }
    }

    @Override
    public Chat banUserInChat(Long chatId, Long userId) {
        User user = userService.get(userId);
        Chat chat = getChatById(chatId);

        if (chat.getBannedUsers().contains(user)){
            throw new DataNotFoundException(StringConstants.USER_BY_SUCH_ID_IS_ALREADY_EXISTS_IN_LIST_OF_BANNED_USERS_IN_THIS_CHAT,
                    ExceptionCode.DUPLICATE_VALUE);
        } else {
            chat.getBannedUsers().add(user);
            chat = updateChat(chat, chatId);
            return chat;
        }
    }

    @Override
    public Chat unBanUserFromChat(Long chatId, Long userId) {

        User user = userService.get(userId);
        Chat chat = getChatById(chatId);

        if(chat.getBannedUsers().contains(user)){

            chat.getBannedUsers().remove(user);
            chat = updateChat(chat, chatId);

            return chat;
        }else{
            throw new DataNotFoundException(StringConstants.USER_BY_SUCH_ID_IS_NOT_BE_BANNED_IN_THIS_CHAT,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public Long getCountOfMessages(Long chatId) {
        if (chatRepository.existsById(chatId)){
            return messageRepository.countByChatId(chatId);
        } else {
            throw new DataNotFoundException(StringConstants.CHAT_ID_SHOULD_BE_POSITIVE,
                    ExceptionCode.NOT_FOUND);
        }
    }
}
