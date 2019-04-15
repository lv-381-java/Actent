package com.softserve.actent.model.dto;

import com.softserve.actent.model.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SubscribeDto {

    String city;

    List<Category> categories;

}
