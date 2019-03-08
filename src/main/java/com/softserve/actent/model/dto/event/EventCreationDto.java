package com.softserve.actent.model.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class EventCreationDto {

    @NonNull
    private String title;

    private String description;

    @NonNull
    private Long startDate;

    @NonNull
    private Long duration;

    @NonNull
    private Long creatorId;

    private Long imageId;

    private Integer capacity;

    @NonNull
    private LocationForEventDto locationForEventDto;

    private List<EquipmentForEventDto> equipmentDtoList;

    @NonNull
    private String accessType;

    @NonNull
    private Long categoryId;
}