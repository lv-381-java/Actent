package com.softserve.actent.model.dto.eventUser;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.model.entity.EventUserType;
import com.softserve.actent.utils.enumValidator.Enum;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class EventUserDto {

    @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
    @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long eventId;

    @NotNull(message = StringConstants.USER_ID_CAN_NOT_BE_NULL)
    @Positive(message = StringConstants.USER_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long userId;

    @Enum(enumClass = EventUserType.class, ignoreCase = true,
            message = StringConstants.EVENT_USER_TYPE_IS_NOT_CORRECT)
    private String eventUserType;

}
