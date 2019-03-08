package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.ResourceNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.security.AccessDeniedException;
import com.softserve.actent.exceptions.validation.IncorrectEmailException;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    User user;

    @Transactional
    @Override
    public User add(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            return userRepository.save(user);
        } else {
            log.error("There is a user with such email. Cannot register!");
            throw new IncorrectEmailException("This email already used!", ExceptionCode.INCORRECT_EMAIL);
        }
    }

    @Transactional
    @Override
    public User update(User user, Long id) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        } else {
            throw new AccessDeniedException("User not registered", ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", ExceptionCode.NOT_FOUND));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional
    @Override
    public void delete(Long id) {

        userRepository.deleteById(id);
    }
}
