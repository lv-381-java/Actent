package com.softserve.actent.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class CreateEquipmentDto {

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private boolean satisfied;

    private Long userId;

    @NonNull
    private Long eventId;

}
