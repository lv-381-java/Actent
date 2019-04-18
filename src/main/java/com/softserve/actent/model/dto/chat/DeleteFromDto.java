package com.softserve.actent.model.dto.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteFromDto {

    private Long messageId;

    private Long senderId;
}
