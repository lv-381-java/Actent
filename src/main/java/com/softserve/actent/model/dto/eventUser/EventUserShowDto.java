package com.softserve.actent.model.dto.eventUser;

import com.softserve.actent.model.entity.EventUserType;
import lombok.Data;

@Data
public class EventUserShowDto {

    private Long id;
    private Long eventId;
    private Long userId;
    private EventUserType eventUserType;
}
