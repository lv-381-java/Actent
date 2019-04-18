package com.softserve.actent.service;

import com.softserve.actent.model.entity.Chat;

public interface ChatService {

    Chat addChat(String type);

    Chat getChatById(Long chatId);

    void deleteChatById(Long chatId);

    Chat banUserInChat(Long chatId, Long userId);

    Chat updateChat(Chat chat, Long chatId);
    
    Chat unBanUserFromChat(Long chatId, Long userId);

    Long getCountOfMessages(Long chatId);
}
