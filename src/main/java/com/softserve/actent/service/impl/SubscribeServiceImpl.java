package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.Subscribe;
import com.softserve.actent.notification.EmailNotification;
import com.softserve.actent.repository.SubscribeRepository;
import com.softserve.actent.service.SubscribeService;
import com.softserve.actent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import static com.softserve.actent.exceptions.codes.ExceptionCode.NOT_FOUND;

@Service
public class SubscribeServiceImpl implements SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final EmailNotification emailNotification;
    private final UserService userService;


    @Autowired
    public SubscribeServiceImpl(SubscribeRepository subscribeRepository,
                                EmailNotification emailNotification,
                                UserService userService) {
        this.subscribeRepository = subscribeRepository;
        this.emailNotification = emailNotification;
        this.userService = userService;
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
            throw new DataNotFoundException(ExceptionMessages.SUBSCRIPTION_NOT_FOUND, NOT_FOUND);
        }
    }

    @Override
    public List<Subscribe> getAllByUserId(Long userId) {

        return subscribeRepository.findAllBySubscriber(userService.get(userId));
    }

    @Override
    public void checkSubscribers(Event event) {
        String category = event.getCategory().getName();
        String city = event.getAddress().getAddress();
        List<Subscribe> subscribes = subscribeRepository.findAllByCategoryAndCity(category, city);
        if (!subscribes.isEmpty()) {
            StringJoiner address = new StringJoiner(",");
            for (Subscribe subscribe : subscribes) {
                address.add(subscribe.getSubscriber().getEmail());
            }
            sendNotificationToSubscriber(address.toString(), event);
        }

    }


    private void sendNotificationToSubscriber(String address, Event event) {
        String subject = "Actent event: " + event.getTitle() + "was created";
        String content = "Created new event for more details please go over the link: "
                + UrlConstants.EVENT_LINK + event.getId();

        emailNotification.sendEmail(address, subject, content);
    }

}
