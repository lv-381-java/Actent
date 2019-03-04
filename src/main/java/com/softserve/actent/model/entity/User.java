package com.softserve.actent.model.entity;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_USER_FIRST_NAME)
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_USER_LAST_NAME)
    @Column(nullable = false)
    private String lastName;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_USER_LOGIN)
    @Column(unique = true, nullable = false)
    private String login;

    @NotNull
    @NotBlank(message = StringConstants.EMPTY_USER_EMAIL)
    @Column(unique = true, nullable = false)
    private String email;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_USER_PASSWORD)
    @Column(nullable = false)
    private String password;

    @NonNull
    @NotNull(message = StringConstants.EMPTY_USER_BIRTH_DATE)
    @Column(nullable = false)
    private LocalDate birthDate;

    @NonNull
    @ManyToOne
    private Image avatar;

    @NonNull
    @NotNull(message = StringConstants.EMPTY_USER_LOCATION)
    @ManyToOne
    private Location location;

    @NonNull
    @Length(max = 500)
    @Column(length = 500)
    private String bio;

    @NonNull
    @ManyToMany
    @JoinTable(name = "user_categories",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "interests_id")})
    private Set<Category> interests = new HashSet<>();

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_USER_SEX)
    @Column(nullable = false)
    private String sex;

    @NonNull
    @ManyToMany
    private Set<Event> events = new HashSet<>();

    @NonNull
    @OneToMany
    @JoinColumn(nullable = false)
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_chat",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")})
    private Set<Chat> bannedChats = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Role role;

}
