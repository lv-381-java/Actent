package com.softserve.actent.model.entity;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Entity
@Table(name = "equipments")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = StringConstants.TITLE_SHOULD_NOT_BE_BLANK)
    @Column(nullable = false)
    private String title;

    @NonNull
    @NotBlank(message = StringConstants.DESCRIPTION_SHOULD_NOT_BE_BLANK)
    @Column(nullable = false)
    private String description;

    @NonNull
    private boolean satisfied;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User assignedUser;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Event assignedEvent;

}
