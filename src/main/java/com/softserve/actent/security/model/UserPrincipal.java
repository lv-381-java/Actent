package com.softserve.actent.security.model;

import com.softserve.actent.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

    private Long id;

    private String firstName;

    private String lastName;

    private String login;

    private String email;

    private String password;

    private LocalDate birthDate;

    private Image avatar;

    private Location location;

    private String bio;

    private List<Category> interests;

    private Sex sex;

    private List<EventUser> eventUsers;

    private List<Review> reviews;

    private List<Chat> bannedChats;

    private Set<Role> roleset;

    private Map<String, Object> attributes;

    Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String firstName, String lastName, String login, String email,
                         String password, LocalDate birthDate, Image avatar, Location location,
                         String bio, List<Category> interests, Sex sex, List<EventUser> eventUsers,
                         List<Review> reviews, List<Chat> bannedChats, Set<Role> roleset,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.avatar = avatar;
        this.location = location;
        this.bio = bio;
        this.interests = interests;
        this.sex = sex;
        this.eventUsers = eventUsers;
        this.reviews = reviews;
        this.bannedChats = bannedChats;
        this.roleset = roleset;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {

//        List<GrantedAuthority> authorities = user.getRoleset().stream().map(role ->
//                new SimpleGrantedAuthority(role.name())
//        ).collect(Collectors.toList());
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getBirthDate(),
                user.getAvatar(),
                user.getLocation(),
                user.getBio(),
                user.getInterests(),
                user.getSex(),
                user.getEventUsers(),
                user.getReviews(),
                user.getBannedChats(),
                user.getRoleset(),
                authorities
        );
    }
    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
