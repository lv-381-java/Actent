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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = StringConstants.REGION_SHOULD_NOT_BE_BLANK)
    @Length(max = 30, message = StringConstants.REGION_NO_LONGER_THAN_THIRTY_SYMBOLS)
    @Column(unique = true, nullable = false, length = 30)
    private String name;

    @NonNull
    @ManyToOne
    private Country country;

    @NonNull
    @OneToMany(mappedBy = "region")
    private Set<City> cities = new HashSet<>();
}
