package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.validation.MessageValidationException;
import com.softserve.actent.model.entity.Image;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.model.entity.MessageType;
import com.softserve.actent.repository.MessageRepository;
import com.softserve.actent.service.ImageService;
import com.softserve.actent.service.MessageService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.softserve.actent.exceptions.codes.ExceptionCode.MESSAGE_NOT_FOUND;

@Service
@NoArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private ImageService imageService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, ImageService imageService) {
        this.messageRepository = messageRepository;
        this.imageService = imageService;
    }

    @Override
    @Transactional
    public Message add(Message message) {
        Message newMessage = new Message();
        newMessage.setChat(message.getChat());
        newMessage.setSender(message.getSender());
        newMessage.setMessageContent(message.getMessageContent());
        newMessage.setMessageType(MessageType.TEXT);
        return messageRepository.save(newMessage);
    }

    @Override
    @Transactional
    public Message addImageMessage(Message message) {
        Image image = new Image();
        Message newMessage = new Message();
        image.setFilePath(message.getImage().getFilePath());
        newMessage.setMessageType(MessageType.IMAGE);
        newMessage.setImage(imageService.add(image));
        newMessage.setSender(message.getSender());
        newMessage.setChat(message.getChat());
        return messageRepository.save(newMessage);
    }

    @Override
    public List<Message> getAll() {

        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) {

            throw new DataNotFoundException(
                    ExceptionMessages.MESSAGE_NOT_FOUND,
                    MESSAGE_NOT_FOUND
            );
        } else {

            return messages;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(id);
        } else {
            throw new DataNotFoundException(ExceptionMessages.MESSAGE_NOT_FOUND, MESSAGE_NOT_FOUND);
        }
    }

    @Override
    public Message get(Long id) {
        return messageRepository.findById(id).
                orElseThrow(() -> new DataNotFoundException(ExceptionMessages.MESSAGE_NOT_FOUND, MESSAGE_NOT_FOUND));
    }

    @Override
    @Transactional
    public Message update(Message message, Long id) {
        Message newMessage = new Message();
        newMessage.setChat(message.getChat());
        newMessage.setSender(message.getSender());
        newMessage.setMessageContent(message.getMessageContent());

        Optional<Message> optionalMessage = messageRepository.findById(id);

        if (optionalMessage.isPresent() && checkCredential(optionalMessage, newMessage)) {

            newMessage.setId(id);
            newMessage.setMessageType(MessageType.TEXT);
            return messageRepository.save(newMessage);
        } else {
            throw new DataNotFoundException(ExceptionMessages.MESSAGE_NOT_FOUND, MESSAGE_NOT_FOUND);
        }
    }

    @Override
    public List<Message> getCurrentMessagesByChatId(Long chatId, Pageable pageable) {
        return messageRepository.findAllByChatId(chatId, pageable).getContent();

    }

    @Override
    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findAllByChatId(chatId);

    }

    private boolean checkCredential(Optional<Message> optionalMessage, Message message) {

        if (optionalMessage.get().getChat().getId().equals(message.getChat().getId()) &&
                optionalMessage.get().getSender().getId().equals(message.getSender().getId())) {
            return true;
        } else
            throw new MessageValidationException(ExceptionMessages.YOU_CAN_NOT_CHANGE_THIS_MESSAGE,
                    ExceptionCode.VALIDATION_FAILED);


    }

}
