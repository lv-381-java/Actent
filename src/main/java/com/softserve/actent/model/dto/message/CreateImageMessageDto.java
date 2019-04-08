package com.softserve.actent.model.dto.message;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CreateImageMessageDto {

    @Positive(message = StringConstants.CHAT_ID_SHOULD_BE_POSITIVE)
    @NotNull(message = StringConstants.CHAT_ID_SHOULD_NOT_BE_NULL)
    private Long chatId;

    @NotBlank(message = StringConstants.IMAGE_FILE_PATH_SHOULD_NOT_BE_BLANK)
    @Length(max = 256, message = StringConstants.TOO_LONG)
    private String imageFilePath;

}
