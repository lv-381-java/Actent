package com.softserve.actent.model.dto.chat;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class ChatTextMessageDto {

    @Positive(message = StringConstants.USER_ID_CAN_NOT_BE_NULL)
    Long senderId;

    @NotBlank(message = StringConstants.MESSAGE_SHOULD_NOT_BE_BLANK)
    String messageContent;

    @Positive
    @NotNull(message = StringConstants.CHAT_ID_SHOULD_NOT_BE_NULL)
    Long chatId;
}
