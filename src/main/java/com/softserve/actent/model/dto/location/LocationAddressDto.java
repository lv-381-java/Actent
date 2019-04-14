package com.softserve.actent.model.dto.location;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class LocationCreateDto {

    @NotBlank(message = StringConstants.EMPTY_LOCATION)
    @Length(min = NumberConstants.LOCATION_MIN_LENGTH,
            max = NumberConstants.LOCATION_MAX_LENGTH,
            message = StringConstants.LOCATION_LENGTH_BETWEEN_FIVE_AND_FIFTY_SYMBOLS)
    private String address;

}
