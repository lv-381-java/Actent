package com.softserve.actent.model.dto.chat;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class ChatImageMessageDto {

    @Positive(message = StringConstants.USER_ID_CAN_NOT_BE_NULL)
    private Long senderId;

    @NotBlank(message = StringConstants.IMAGE_FILE_PATH_SHOULD_NOT_BE_BLANK)
    private String filePath;

    @Positive
    @NotNull(message = StringConstants.CHAT_ID_SHOULD_NOT_BE_NULL)
    private Long chatId;

}
