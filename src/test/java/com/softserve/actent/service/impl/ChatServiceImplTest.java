package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.validation.IncorrectChatTypeException;
import com.softserve.actent.model.entity.Chat;
import com.softserve.actent.model.entity.ChatType;
import com.softserve.actent.model.entity.Message;
import com.softserve.actent.model.entity.MessageType;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.ChatRepository;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.ChatService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.internal.verification.VerificationModeFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatServiceImplTest {

    @Autowired
    private ChatService chatService;

    @MockBean
    private ChatRepository chatRepository;

    @MockBean
    private UserRepository userRepository;

    private Long existingId = 1L;
    private Long nonExistingId = -1L;

    private Long existingUserId = 2L;
    private Long existingUserId3 = 3L;
    private ChatType chatType = ChatType.EVENT;
    private Message message1 = new Message();
    private Message message2 = new Message();
    private User user1 = new User();
    private User user2 = new User();
    private User user3 = new User();
    private List<Message> messages;
    private List<User> bannedUsers;
    private List<User> bannedUserExists;
    private List<User> unBannedUserExists;
    Chat chat = null;

    @Before
    public void setUp(){
        chat = new Chat();
        chat.setId(existingId);
        chat.setType(chatType);

        user1.setId(1L);
        user1.setFirstName("Valentyn1");
        user1.setLastName("Yarmoshyk1");
        user1.setLogin("loginValentyn1");
        user1.setPassword("passwordValentyn1");

        user2.setId(existingUserId);
        user2.setFirstName("Valentyn2");
        user2.setLastName("Yarmoshyk2");
        user2.setLogin("loginValentyn2");
        user2.setPassword("passwordValentyn2");

        user3.setId(existingUserId3);
        user3.setFirstName("Valentyn3");
        user3.setLastName("Yarmoshyk3");
        user3.setLogin("loginValentyn3");
        user3.setPassword("passwordValentyn3");

        bannedUsers = new LinkedList<>(Arrays.asList(user1, user3));
        chat.setBannedUsers(bannedUsers);
        bannedUserExists = Arrays.asList(user1, user2, user3);
        unBannedUserExists = Arrays.asList(user1);

        message1.setChat(chat);
        message1.setSender(user1);
        message1.setMessageType(MessageType.TEXT);
        message1.setMessageContent("Hello, world1");

        message2.setChat(chat);
        message2.setSender(user2);
        message2.setMessageType(MessageType.TEXT);
        message2.setMessageContent("Hello, world2");

        messages = Arrays.asList(message1, message2);
        chat.setMessages(messages);

        Mockito.when(chatRepository.findById(existingId))
                .thenReturn(Optional.of(chat));
        Mockito.when(chatRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());
        Mockito.when(chatRepository.save(chat))
                .thenReturn(chat);
        Mockito.doNothing().when(chatRepository)
                .deleteById(existingId);
        Mockito.when(chatRepository.existsById(existingId))
                .thenReturn(true);
        Mockito.when(chatRepository.existsById(nonExistingId))
                .thenReturn(false);
        Mockito.when(userRepository.findById(existingUserId))
                .thenReturn(Optional.of(user2));
        Mockito.when(userRepository.findById(existingUserId3))
                .thenReturn(Optional.of(user3));
    }

    @After
    public void tearDown(){
        chat = null;
        user1 = null;
        user2 = null;
        user3 = null;
        message1 = null;
        message2 = null;
        messages = null;
        bannedUsers = null;
        bannedUserExists = null;
    }

    @Test
    public void whenValidId_thenChatShouldBeFound(){
        Chat found = chatService.getChatById(existingId);
        verifyFindByIdIsCalledOnce();
        assertThat(found.getType()).isEqualTo(chatType);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenNonValidId_thenEmployeeShouldNotBeFound(){
        Chat found = chatService.getChatById(nonExistingId);
        verifyFindByIdIsCalledOnce();
        assertThat(found).isNull();
    }

    @Test
    public void whenAddChat_thenChatShouldBeReturned(){
        Chat chat = chatService.addChat("event");
        assertThat(chat).isEqualTo(chat);
    }

    @Test(expected = IncorrectChatTypeException.class)
    public void whenAddNonExistingChat_thenChatShouldNotBeFound(){
        Chat chat = chatService.addChat("NonExistingChatType");
        assertThat(chat).isNull();
    }

    @Test
    public void whenDeleteChatWithExistingId_thenNothingShouldBeReturned(){
        chatService.deleteChatById(existingId);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenDeleteChatWithNonExistingId_thenExceptionShouldBeThrown(){
        chatService.deleteChatById(nonExistingId);
    }

    @Test
    public void whenUpdateChatWithExistingId_thenChatShouldBeReturned(){
        assertThat(chatService.updateChat(chat, existingId).getType()).isEqualTo(chatType);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenUpdateChatWithNonExistingId_thenExceptionShouldBeThrown(){
        assertThat(chatService.updateChat(chat, nonExistingId).getType()).isEqualTo(chatType);
    }

    @Test
    public void whenAddBannedUserToChat_thenListBannedUsersShouldBeReturned(){
        Chat chat = chatService.banUserInChat(existingId, existingUserId);
        assertThat(chat.getBannedUsers().size())
                .isEqualTo(bannedUserExists.size());
    }

    @Test(expected = DataNotFoundException.class)
    public void whenDeleteBannedNonExistsUserFromChat_thenExceptionShouldBeThrown(){
        Chat chat = chatService.unBanUserFromChat(existingId, existingUserId);
        assertThat(chat.getBannedUsers().size())
                .isEqualTo(unBannedUserExists.size());
    }

    @Test
    public void whenDeleteBannedNonExistsUserFromChat_thenListBannedUsersShouldBeReturned(){
        Chat chat = chatService.unBanUserFromChat(existingId, existingUserId3);
        assertThat(chat.getBannedUsers().size())
                .isEqualTo(unBannedUserExists.size());
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(chatRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
        Mockito.reset(chatRepository);
    }
}