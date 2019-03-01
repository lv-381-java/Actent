package com.softserve.actent.model.entity;

import com.softserve.actent.resources.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_REWIEW_TEXT)
    @Column(nullable = false)
    private String text;

    @NonNull
    @NotNull(message = StringConstants.NO_REVIEW_SCORE)
    @Column(nullable = false)
    private Integer score;

    @ManyToOne
    private Event event;

    @ManyToOne
    private User user;
}



