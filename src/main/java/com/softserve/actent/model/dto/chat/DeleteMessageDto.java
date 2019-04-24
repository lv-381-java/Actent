package com.softserve.actent.model.dto.chat;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class DeleteMessageDto {

    @Positive(message = StringConstants.USER_ID_CAN_NOT_BE_NULL)
    Long senderId;

    @Positive
    @NotNull(message = StringConstants.MESSAGE_ID_SHOULD_BE_POSITIVE)
    Long id;

    private String delete;
}
