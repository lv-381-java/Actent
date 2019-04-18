package com.softserve.actent.model.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MinimalEventDto implements UltraEventDto {

    private Long id;
    private String title;
    private String description;
    private String creationDate;
    private LocalDateTime startDate;
    private Long duration;
    private Integer capacity;
    private String accessType;

}
