package com.softserve.actent.repository;

import com.softserve.actent.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    Optional<User> findByLoginOrEmail(String login, String email);

    Optional<User> findUserByLogin(String login);

    Optional<User> findByProviderId(String providerId);

    Boolean existsByEmailAndProviderIdNull(String Email);
}
