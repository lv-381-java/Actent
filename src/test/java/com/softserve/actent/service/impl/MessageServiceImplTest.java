package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.validation.ValidationException;
import com.softserve.actent.model.entity.Chat;
import com.softserve.actent.model.entity.Image;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.MessageRepository;
import com.softserve.actent.service.ImageService;
import com.softserve.actent.service.MessageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    ImageService imageService;

    @MockBean
    private MessageRepository messageRepository;

    private final Long firstId = 1L;
    private final Long secondId = 2L;
    private final Long nonExistingId = -1L;
    private final int messageCount = 2;
    private final Long chatId = 1L;
    private final String firstMessageContent = "firstMessageContent";
    private final String imageFilePath = "imageFilePath";
    List<Message> messages;
    private Message messageWithText;
    private Message messageWithImage;
    private User user;
    private Chat chat;
    private Image image;

    @Before
    public void setUp() {

        messageWithText = new Message();
        messageWithImage = new Message();
        messageWithText.setId(firstId);
        messageWithText.setMessageContent(firstMessageContent);
        messageWithImage.setId(secondId);


        image = new Image();
        user = new User();
        chat = new Chat();
        user.setId(firstId);
        chat.setId(firstId);
        image.setFilePath(imageFilePath);

        messageWithText.setSender(user);
        messageWithText.setChat(chat);
        messageWithImage.setSender(user);
        messageWithImage.setChat(chat);
        messageWithImage.setImage(image);

        messages = Arrays.asList(messageWithText, messageWithImage);

        Mockito.when(messageRepository.findById(firstId)).thenReturn(Optional.of(messageWithText));
        Mockito.when(messageRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(messageRepository.findAllByChatId(chatId)).thenReturn(messages);
        Mockito.when(messageRepository.findAll()).thenReturn(messages);
        Mockito.when(messageRepository.save(messageWithText)).thenReturn(messageWithText);
        Mockito.when(messageRepository.save(messageWithImage)).thenReturn(messageWithImage);
        Mockito.when(imageService.add(image)).thenReturn(image);
        Mockito.doNothing().when(messageRepository).deleteById(firstId);
        Mockito.doNothing().when(messageRepository).deleteById(secondId);

    }

    @Test
    public void whenValidId_thenMessageShouldBeFound() {

        assertThat(messageWithText).isEqualTo(messageService.get(firstId));

    }

    @Test(expected = DataNotFoundException.class)
    public void whenInValidId_thenExceptionShouldBeThrown() {
        messageService.get(nonExistingId);
    }

    @Test
    public void given2Messages_findAllByChatId_thenReturn2Records() {
        assertThat(messageService.getMessagesByChatId(chatId).size()).isEqualTo(messageCount);
    }

    @Test
    public void given2Messages_findAll_thenReturn2Records() {
        assertThat(messageService.getAll().size()).isEqualTo(messageCount);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenFindAllIsEmpty_thenExceptionShouldBeThrown() {
        Mockito.when(messageRepository.findAll()).thenReturn(Collections.emptyList());
        assertThat(messageService.getAll().size()).isEqualTo(messageCount);
    }

    @Test(expected = ValidationException.class)
    public void whenUpdateMessage_thenExceptionShouldBeThrown() {
        Message newMessage = new Message();
        newMessage.setId(firstId);
        newMessage.setMessageContent(firstMessageContent);

        Chat newChat = new Chat();
        User newUser = new User();
        newUser.setId(firstId);
        newChat.setId(secondId);

        newMessage.setSender(newUser);
        newMessage.setChat(newChat);
        assertThat(messageService.update(newMessage, firstId)).isEqualTo(newMessage);
    }

    @Test
    public void whenDeleteMessageWithExistingId_thenNothingShouldBeReturned() {

        messageService.delete(firstId);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenDeleteMessageWithNonExistingId_thenExceptionShouldBeThrown() {

        messageService.delete(nonExistingId);
    }

    @After
    public void tearDown() {
        user = null;
        chat = null;
        messageWithText = null;
        messageWithImage = null;
        image = null;
        assertNull(user);
        assertNull(chat);
        assertNull(messageWithText);
        assertNull(messageWithImage);

    }

}
