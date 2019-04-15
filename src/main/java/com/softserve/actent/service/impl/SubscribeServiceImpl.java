package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.Subscribe;
import com.softserve.actent.repository.SubscribeRepository;
import com.softserve.actent.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.softserve.actent.exceptions.codes.ExceptionCode.MESSAGE_NOT_FOUND;

@Service
public class SubscribeServiceImpl implements SubscribeService {
private final SubscribeRepository subscribeRepository;

    @Autowired
    public SubscribeServiceImpl(SubscribeRepository subscribeRepository) {
        this.subscribeRepository = subscribeRepository;
    }

    @Override
    public Subscribe add(Subscribe subscribe) {
        return subscribeRepository.save(subscribe);
    }

    @Override
    public void delete(Long id) {
        Optional<Subscribe> optionalSubscribe = subscribeRepository.findById(id);
        if (optionalSubscribe.isPresent()) {
            subscribeRepository.deleteById(id);
        } else {
            //todo change return exception message
            throw new DataNotFoundException(ExceptionMessages.MESSAGE_NOT_FOUND, MESSAGE_NOT_FOUND);
        }
    }

    @Override
    public List<Subscribe> getAll() {

        return null;
    }
}
