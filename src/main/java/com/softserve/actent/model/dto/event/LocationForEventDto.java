package com.softserve.actent.model.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
class LocationForEventDto {

    @NonNull
    private Long cityId;
    private String address;
}