package com.softserve.actent.model.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
class EquipmentForEventDto {

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private boolean satisfied;
}