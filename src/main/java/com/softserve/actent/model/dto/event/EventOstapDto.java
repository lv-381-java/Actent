package com.softserve.actent.model.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.actent.model.entity.Image;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventOstapDto extends MinimalEventDto {

    @JsonProperty("Location")
    private LocationForEventDto locationForEventDto;

    @JsonProperty("Category")
    private CategoryForEventDto categoryForEventDto;

    private Image image;
}
