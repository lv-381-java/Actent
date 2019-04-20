package com.softserve.actent.model.dto.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserForEventDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String sex;
    private String role;
    private String birthDate;
}
