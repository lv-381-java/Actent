package com.softserve.actent.security.model;

import com.softserve.actent.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class UserPrincipal implements UserDetails {

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

    Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {

        List<GrantedAuthority> authorities = user.getRoleset().stream().map(role ->
                new SimpleGrantedAuthority(role.name())
        ).collect(Collectors.toList());

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
}
