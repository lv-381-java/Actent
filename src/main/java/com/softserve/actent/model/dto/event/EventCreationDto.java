package com.softserve.actent.model.dto.event;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventCreationDto {

    @NotBlank(message = StringConstants.TITLE_SHOULD_NOT_BE_BLANK)
    @Length(max = NumberConstants.TITLE_MAX_LENGTH, message = StringConstants.TITLE_LENGTH_IS_TO_LONG)
    private String title;

    @NotBlank(message = StringConstants.DESCRIPTION_SHOULD_NOT_BE_BLANK)
    @Length(min = NumberConstants.MIN_VALUE_FOR_DESCRIPTION,
            max = NumberConstants.MAX_VALUE_FOR_DESCRIPTION,
            message = StringConstants.DESCRIPTION_SHOULD_BE_BETWEEN_MIN_AND_MAX_VALUE)
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Future(message = StringConstants.START_DATE_CAN_NOT_BE_PAST)
    @NotNull(message = StringConstants.START_DATE_CAN_NOT_BE_NULL)
    private LocalDateTime startDate;

    @Min(value = NumberConstants.DURATION_MIN_VALUE, message = StringConstants.DURATION_CAN_NOT_BE_LESS_THAN_TEN_MINUTES)
    @NotNull(message = StringConstants.DURATION_CAN_NOT_BE_NULL)
    @Positive(message = StringConstants.DURATION_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long duration;

    @NotNull(message = StringConstants.CREATOR_ID_CAN_NOT_BE_NULL)
    @Positive(message = StringConstants.CREATOR_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long creatorId;

    @Positive(message = StringConstants.IMAGE_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO_ALSO_IT_CAN_BE_NULL)
    private Long imageId;

    @Positive(message = StringConstants.CAPACITY_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO_ALSO_IT_CAN_BE_NULL)
    private Integer capacity;

    @NotNull(message = StringConstants.LOCATION_MUST_BE_NOT_NULL)
    @Positive(message = StringConstants.LOCATION_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long locationId;

    @NotBlank(message = StringConstants.ACCESS_TYPE_CAN_NOT_BE_NULL_OR_EMPTY)
    private String accessType;

    @NotNull(message = StringConstants.CATEGORY_ID_CAN_NOT_BE_NULL)
    @Positive(message = StringConstants.CATEGORY_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
    private Long categoryId;
}
