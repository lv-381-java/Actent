package com.softserve.actent.model.entity;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = StringConstants.LOCATION_SHOULD_NOT_BE_BLANK)
    @Length(max = 50, message = StringConstants.LOCATION_NO_LONGER_THAN_FIFTY_SYMBOLS)
    @Column(unique = true, nullable = false, length = 50)
    private String address;

    @NonNull
    @ManyToOne
    private City city;
}
