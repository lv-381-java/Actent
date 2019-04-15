package com.softserve.actent.model.entity;

import com.softserve.actent.constant.NumberConstants;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Entity
@Table(name = "locations", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"address", "latitude", "longtitude"})})
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_LOCATION)
    @Length(min = NumberConstants.LOCATION_MIN_LENGTH,
            max = NumberConstants.LOCATION_MAX_LENGTH,
            message = StringConstants.LOCATION_SHOULD_BE_BETWEEN_2_AND_100_SYMBOLS)
    @Column(nullable = false,
            length = NumberConstants.LOCATION_MAX_LENGTH)
    private String address;

    private Double latitude;

    private Double longtitude;
}
