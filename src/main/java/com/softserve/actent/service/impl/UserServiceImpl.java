package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.DuplicateValueException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.security.AccessDeniedException;
import com.softserve.actent.exceptions.validation.IncorrectEmailException;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.LocationService;
import com.softserve.actent.service.ImageService;
import com.softserve.actent.service.UserService;
import com.softserve.actent.service.cache.EventCache;
import com.softserve.actent.service.cache.EventCacheMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final EventCache eventCache;
    private final UserRepository userRepository;
    private final LocationService locationService;
    private final ImageService imageService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           LocationService locationService,
                           ImageService imageService,
                           EventCache eventCache) {

        this.userRepository = userRepository;
        this.locationService = locationService;
        this.imageService = imageService;
        this.eventCache = eventCache;
    }

    @Transactional
    @Override
    public User add(User user) {

        if (!userRepository.existsByEmail(user.getEmail())) {
            if (!userRepository.existsByLogin(user.getLogin())){
                Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
                return optionalUser.orElseGet(() -> userRepository.save(user));
            } else {
                throw new DuplicateValueException(ExceptionMessages.USER_BY_THIS_LOGIN_ALREADY_EXIST, ExceptionCode.DUPLICATE_VALUE);
            }
        } else {
            throw new IncorrectEmailException(ExceptionMessages.EMAIL_ALREADY_USED, ExceptionCode.INCORRECT_EMAIL);
        }
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public User update(User user, Long id) {

        if (userRepository.existsById(id)) {

            User existingUser = this.get(id);
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setLogin(user.getLogin());
            existingUser.setPhone(user.getPhone());
            existingUser.setEmail(user.getEmail());
            existingUser.setBirthDate(user.getBirthDate().plusDays(1L));
            existingUser.setBio(user.getBio());

            if (user.getLocation() != null){
                existingUser.setLocation(locationService.get(user.getLocation().getId()));
            }
            if (user.getAvatar() != null){
                existingUser.setAvatar(imageService.get(user.getAvatar().getId()));
            }
            eventCache.cacheRefresh(id, EventCacheMethod.CREATOR);
            return userRepository.save(existingUser);

        } else {
            throw new AccessDeniedException(ExceptionMessages.USER_NOT_REGISTRED, ExceptionCode.NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public User registrationUpdate(User user, Long id) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            eventCache.cacheRefresh(id, EventCacheMethod.CREATOR);
            return userRepository.save(user);
        } else {
            throw new AccessDeniedException(ExceptionMessages.USER_NOT_REGISTRED, ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.USER_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new DataNotFoundException(ExceptionMessages.USER_BY_THIS_EMAIL_IS_NOT_FOUND, ExceptionCode.NOT_FOUND));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            eventCache.cacheRefresh(id, EventCacheMethod.CREATOR);
            userRepository.deleteById(id);
        } else {
            throw new DataNotFoundException(ExceptionMessages.USER_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login).orElseThrow(() ->
                new DataNotFoundException(ExceptionMessages.USER_BY_THIS_LOGIN_IS_NOT_FOUND, ExceptionCode.NOT_FOUND));
    }
}
