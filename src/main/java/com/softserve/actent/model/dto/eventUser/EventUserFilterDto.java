package com.softserve.actent.model.dto.eventUser;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.model.entity.EventUserType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class EventUserFilterDto {

    @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
    @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long eventId;

    @NotBlank(message = StringConstants.TITLE_SHOULD_NOT_BE_BLANK)
    @Length(max = NumberConstants.TITLE_MAX_LENGTH, message = StringConstants.TITLE_LENGTH_IS_TO_LONG)
    private String eventTitle;

    @NotBlank(message = StringConstants.DESCRIPTION_SHOULD_NOT_BE_BLANK)
    @Length(min = NumberConstants.MIN_VALUE_FOR_DESCRIPTION,
            max = NumberConstants.MAX_VALUE_FOR_DESCRIPTION,
            message = StringConstants.DESCRIPTION_SHOULD_BE_BETWEEN_MIN_AND_MAX_VALUE)
    private String eventDescription;

    @NotNull(message = StringConstants.USER_ID_CAN_NOT_BE_NULL)
    @Positive(message = StringConstants.USER_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long userId;

    private EventUserType eventUserType;
}
