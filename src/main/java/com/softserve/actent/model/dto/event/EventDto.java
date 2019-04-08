package com.softserve.actent.model.dto.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.actent.model.dto.equipment.EquipmentDto;
import com.softserve.actent.model.dto.eventUser.EventUserForEventDto;
import com.softserve.actent.model.entity.Review;
import com.softserve.actent.model.entity.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EventDto {

    private Long id;
    private String title;
    private String description;
    private String creationDate;
    private LocalDateTime startDate;
    private Long duration;
    private Integer capacity;

    @JsonProperty("Creator")
    private UserForEventDto userForEventDto;

    @JsonProperty("Location")
    private LocationForEventDto locationForEventDto;

    @JsonProperty("Equipments")
    private List<EquipmentDto> equipments;

    private String accessType;

    private List<EventUserForEventDto> eventForEventUserDtos;

    @JsonProperty("Category")
    private CategoryForEventDto categoryForEventDto;

    private List<Tag> tags;

    @JsonProperty("Chat")
    private ChatForEventDto chatForEventDto;

    private List<Review> feedback;
}
