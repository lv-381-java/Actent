package com.softserve.actent.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "Can't be empty")
    @Length(max = 100, message = "Too long")
    @Column(nullable = false, length = 100)
    private String title;

    @Length(max = 500, message = "Too long")
    @Column(length = 500)
    private String description;

    @Column(name = "creation_date", nullable = false)
    private Long creationDate;

    @NonNull
    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @NonNull
    @Column(name = "duration", nullable = false)
    private Long duration;

    @NonNull
    @OneToMany
    private User creator;

    /*@NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location address;*/

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    private Integer capacity;

    /*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "assigned_event")
    private List<Equipment> equipments;*/

    @NonNull
    @Enumerated(value = EnumType.ORDINAL)
    @Column(nullable = false)
    private AccessType accessType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_participants",
            joinColumns = {@JoinColumn(name = "event_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "participant_id", nullable = false)})
    private Set<User> participants;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_spectators",
            joinColumns = {@JoinColumn(name = "event_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "spectators_id", nullable = false)})
    private Set<User> spectators;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /*@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_tags",
            joinColumns = {@JoinColumn(name = "event_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", nullable = false)})
    private List<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;*/

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "event")
    private List<Review> feedback;
}
