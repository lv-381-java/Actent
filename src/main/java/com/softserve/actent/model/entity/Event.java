package com.softserve.actent.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
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
    @NotBlank(message = StringConstants.TITLE_SHOULD_NOT_BE_BLANK)
    @Length(max = 100, message = "Too long")
    @Column(nullable = false, length = 100)
    private String title;

    @Length(max = 500, message = "Too long")
    @Column(length = 500)
    private String description;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @NonNull
    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @NonNull
    @Column(nullable = false)
    private Long duration;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK_event_to_creator_id"))
    private User creator;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false, foreignKey = @ForeignKey(name = "FK_event_to_location_id"))
    private Location address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "FK_event_to_image_id"))
    private Image image;

    private Integer capacity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignedEvent")
    private List<Equipment> equipments;

    @NonNull
    @Enumerated(value = EnumType.ORDINAL)
    @Column(nullable = false)
    private AccessType accessType;

    @ManyToMany
    @JoinTable(name = "event_participants",
            joinColumns = {@JoinColumn(name = "event_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "participant_id", nullable = false)},
            foreignKey = @ForeignKey(name = "FK_event_to_participant_id"),
            inverseForeignKey = @ForeignKey(name = "FK_participant_to_event_id"))
    @JsonManagedReference
    private List<User> participants;

    @ManyToMany
    @JoinTable(name = "event_spectators",
            joinColumns = {@JoinColumn(name = "event_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "spectators_id", nullable = false)},
            foreignKey = @ForeignKey(name = "FK_event_to_spectator_id"),
            inverseForeignKey = @ForeignKey(name = "FK_spectator_to_event_id"))
    @JsonManagedReference
    private List<User> spectators;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_event_to_category_id"))
    private Category category;

    @ManyToMany
    @JoinTable(name = "event_tags",
            joinColumns = {@JoinColumn(name = "event_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", nullable = false)},
            foreignKey = @ForeignKey(name = "FK_event_to_tag_id"),
            inverseForeignKey = @ForeignKey(name = "FK_tag_to_event_id"))
    private Set<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", foreignKey = @ForeignKey(name = "FK_event_to_chat_id"))
    private Chat chat;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Review> feedback;
}
