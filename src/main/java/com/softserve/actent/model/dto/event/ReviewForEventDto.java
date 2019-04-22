package com.softserve.actent.model.dto.event;

import lombok.Data;

@Data
public class ReviewForEventDto {

    private Long id;
    private String text;
    private Integer score;
    private UserForEventDto author;

}
