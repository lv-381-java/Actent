package com.softserve.actent.model.dto.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class ViewMessageDto {

    private Long id;

    private String sendTime;

    private String senderFirstName;

    private String senderLogin;

    private String messageType;

    private Long senderId;

    private Long chatId;

    private String lastEditTime;
    
    private boolean delete;
}
