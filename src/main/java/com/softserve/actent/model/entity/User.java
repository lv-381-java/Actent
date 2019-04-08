package com.softserve.actent.model.entity;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
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
    @Column(nullable = false, length = NumberConstants.USER_FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_USER_LAST_NAME)
    @Column(nullable = false, length = NumberConstants.USER_LAST_NAME_MAX_LENGTH)
    private String lastName;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_USER_LOGIN)
    @Column(unique = true, nullable = false, length = NumberConstants.USER_LOGIN_MAX_LENGTH)
    private String login;

    @NotNull
    @NotBlank(message = StringConstants.EMPTY_USER_EMAIL)
    @Column(unique = true, nullable = false, length = NumberConstants.USER_EMAIL_MAX_LENGTH)
    @Email
    private String email;

    @NonNull
    @NotBlank(message = StringConstants.EMPTY_USER_PASSWORD)
    @Column(nullable = false)
    private String password;

    @Pattern(regexp="(^$|[0-9]{10})", message = StringConstants.USER_PHONE_IS_NOT_VALID)
    private String phone;

    @Column
    private LocalDate birthDate;

    @NonNull
    @ManyToOne
    private Image avatar;

    @NonNull
    @ManyToOne
    private City location;

    @NonNull
    @Length(max = NumberConstants.USER_BIO_MAX_LENGTH)
    @Column(length = NumberConstants.USER_BIO_MAX_LENGTH)
    private String bio;

    @NonNull
    @ManyToMany
    @JoinTable(name = "user_categories",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "interests_id")})
    private List<Category> interests;

    @Enumerated(EnumType.STRING)
    @Column(length = NumberConstants.USER_SEX_MAX_LENGTH)
    private Sex sex;

    @NonNull
    @OneToMany(mappedBy = "user")
    private List<EventUser> eventUsers;

    @NonNull
    @OneToMany
    @JoinColumn
    private List<Review> reviews;

    @ManyToMany(mappedBy = "bannedUsers")
    private List<Chat> bannedChats;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(length = NumberConstants.USER_ROLE_MAX_LENGTH)
    private Set<Role> roleset;

    @Enumerated(EnumType.STRING)
    @Column(length = NumberConstants.USER_STATUS_MAX_LENGTH)
    private Status status;

    private String uuid;
}
