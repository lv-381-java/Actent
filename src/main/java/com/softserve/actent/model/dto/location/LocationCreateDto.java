package com.softserve.actent.model.dto.location;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
public class LocationCreateDto {

    @NotBlank(message = StringConstants.EMPTY_LOCATION)
    @Length(min = NumberConstants.LOCATION_MIN_LENGTH,
            max = NumberConstants.LOCATION_MAX_LENGTH,
            message = StringConstants.LOCATION_SHOULD_BE_BETWEEN_3_AND_100_SYMBOLS)
    private String address;

    private Double latitude;

    private Double longtitude;
}
