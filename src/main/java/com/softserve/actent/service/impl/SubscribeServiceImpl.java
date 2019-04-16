package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.Subscribe;
import com.softserve.actent.notification.EmailNotification;
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
    private final EmailNotification emailNotification;

    @Autowired
    public SubscribeServiceImpl(SubscribeRepository subscribeRepository,
                                EmailNotification emailNotification) {
        this.subscribeRepository = subscribeRepository;
        this.emailNotification = emailNotification;
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

        return subscribeRepository.findAll();
    }

    @Override
    public void checkSubscribers(Event event) {

        String category = event.getCategory().getName();
        String city = event.getAddress().getCity().getName();
        System.out.println(category + city);
        List<Subscribe> subscribes = subscribeRepository.findAllByCategoryAndCity(category, city);

        if (!subscribes.isEmpty()) {
            StringBuilder address = new StringBuilder();
            for (Subscribe subscribe : subscribes) {
                address.append(subscribe.getSubscriber().getEmail()).append(",");
            }
            address.deleteCharAt(address.lastIndexOf(","));
            sendNotificationToSubscribert(address.toString(), event);
        }

    }


    @Override
    public void checkSubscribers() {

        List<Subscribe> subscribes = subscribeRepository.findAllByCategoryAndCity("sport", "Lviv");
        if (!subscribes.isEmpty()) {
            StringBuilder address = new StringBuilder();
            for (Subscribe subscribe : subscribes) {
                address.append(subscribe.getSubscriber().getEmail()).append(",");
            }
            address.deleteCharAt(address.lastIndexOf(","));
            sendNotificationToSubscribert(address.toString());
        }


    }

    private void sendNotificationToSubscribert(String address) {
        String subject = "Actent event: " + "title" + "was created";
        String content = "Created new event for more details please go over the link: "
                + UrlConstants.EVENT_LINK + 1;

        emailNotification.sendEmail(address, subject, content);
    }


    private void sendNotificationToSubscribert(String address, Event event) {
        String subject = "Actent event: " + event.getTitle() + "was created";
        String content = "Created new event for more details please go over the link: "
                + UrlConstants.EVENT_LINK + event.getId();

        emailNotification.sendEmail(address, subject, content);
    }

}
