package com.softserve.actent.model.dto.user;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserLocationDto {

    private Long id;

    @Size(max = NumberConstants.USER_ADDRESS_MAX_LENGTH, message = StringConstants.USER_LOCATION_LENGHT_RANGE)
    private String address;

}
