package com.softserve.actent.model.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShowEventDto extends EventOstapDto {

    @JsonProperty("Creator")
    private UserForEventDto userForEventDto;

    @JsonProperty("Chat")
    private ChatForEventDto chatForEventDto;

}
