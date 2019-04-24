package com.softserve.actent.model.dto.chat;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class EditFromDto {

    @Positive(message = StringConstants.CHAT_ID_SHOULD_BE_POSITIVE)
    @NotNull(message = StringConstants.CHAT_ID_SHOULD_NOT_BE_NULL)
    private Long chatId;

    @NotBlank(message = StringConstants.MESSAGE_SHOULD_NOT_BE_BLANK)
    private String messageContent;

    @Positive(message = StringConstants.USER_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long senderId;

    @Positive(message = StringConstants.MESSAGE_ID_SHOULD_BE_POSITIVE)
    private Long messageId;
}
