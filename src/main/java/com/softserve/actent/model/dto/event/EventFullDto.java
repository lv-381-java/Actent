package com.softserve.actent.model.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.actent.model.dto.equipment.EquipmentDto;
import com.softserve.actent.model.dto.eventUser.EventUserForEventDto;
import com.softserve.actent.model.entity.Review;
import com.softserve.actent.model.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventFullDto extends ShowEventDto {

    @JsonProperty("Equipments")
    private List<EquipmentDto> equipments;

    private List<EventUserForEventDto> eventForEventUserDtoList;
    private List<Tag> tags;
    private List<Review> feedback;
}
