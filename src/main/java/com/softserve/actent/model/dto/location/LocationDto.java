package com.softserve.actent.model.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDto {

    private Long id;
    private String address;
    private Double latitude;
    private Double longtitude;
}
