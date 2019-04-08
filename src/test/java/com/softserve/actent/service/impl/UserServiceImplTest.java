package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.security.AccessDeniedException;
import com.softserve.actent.model.entity.*;
import com.softserve.actent.repository.CityRepository;
import com.softserve.actent.repository.ImageRepository;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import static junit.framework.TestCase.assertNull;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    private final String cityName = "Lviv";
    private final Long cityId = 1L;

    private final Long firstUserId = 1L;
    private final String firstUserEmail = "vasiliyutkin@gmail.com";
    private final String firstUserLogin = "vasya";
    private final String firstUserName = "vasiliy";
    private final String firstUserLastName = "utkin";
    private final String firstUserPassword = "qwerty";
    private final LocalDate firstUserBirthDate = LocalDate.parse("1977-12-03");
    private final Image firstUserAvatar = new Image();
    private final String firstUserBio = "About first user";
    private final Sex firstUserSex = Sex.MALE;
    private final City firstUserLocation = new City();
    private final Role firstUserRole = Role.ROLE_USER;
    private final String firstUserImagePath = "D:IMG-7033";
    private final Long firstUserImageId = 1L;
    private final Long firstUserLocationId = 1L;
    private final String firstUserLocationAddress = "Fedkovycha str, 5";

    private final Long secondUserId = 2L;
    private final String secondUserEmail = "mamay@gmail.com";
    private final String secondUserLogin = "mamay";
    private final String secondUserName = "kozak";
    private final String secondUserLastName = "mamay";
    private final String secondUserPassword = "qwerty";
    private final LocalDate secondUserBirthDate = LocalDate.parse("1984-05-23");
    private final Image secondUserAvatar = new Image();
    private final String secondUserBio = "About kozak Mamay";
    private final Sex secondUserSex = Sex.MALE;
    private final City secondUserLocation = new City();
    private final Role secondUserRole = Role.ROLE_USER;
    private final String secondUserImagePath = "D:IMG-7035";
    private final Long secondUserImageId = 2L;
    private final Long secondUserLocationId = 2L;
    private final String secondUserLocationAddress = "Pasternaka str, 77";

    private final String nonExistUserEmail = "abrakadabra@gmail.com";
    private final Long nonExistUserId = 999L;
    private final Integer usersCount = 2;

    private Region region = new Region();
    private User firstUser = new User();
    private User secondUser = new User();
    private List<User> users = new ArrayList<>();


    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CityRepository cityRepository;

    @MockBean
    ImageRepository imageRepository;

    @Before
    public void setUp() {

        region.setId(cityId);

        region.setName(cityName);

        firstUserAvatar.setFilePath(firstUserImagePath);
        firstUserAvatar.setId(firstUserImageId);

        secondUserAvatar.setFilePath(secondUserImagePath);
        secondUserAvatar.setId(secondUserImageId);

        firstUserLocation.setId(firstUserLocationId);
        firstUserLocation.setName(firstUserLocationAddress);
        firstUserLocation.setRegion(region);

        secondUserLocation.setId(secondUserLocationId);
        secondUserLocation.setName(secondUserLocationAddress);
        secondUserLocation.setRegion(region);

        firstUser.setId(firstUserId);
        firstUser.setEmail(firstUserEmail);
        firstUser.setLogin(firstUserLogin);
        firstUser.setFirstName(firstUserName);
        firstUser.setLastName(firstUserLastName);
        firstUser.setPassword(firstUserPassword);
        firstUser.setBirthDate(firstUserBirthDate);
        firstUser.setAvatar(firstUserAvatar);
        firstUser.setBio(firstUserBio);
        firstUser.setSex(firstUserSex);
        firstUser.setLocation(firstUserLocation);
        firstUser.setRoleset(Collections.singleton(firstUserRole));

        secondUser.setId(secondUserId);
        secondUser.setEmail(secondUserEmail);
        secondUser.setLogin(secondUserLogin);
        secondUser.setFirstName(secondUserName);
        secondUser.setLastName(secondUserLastName);
        secondUser.setPassword(secondUserPassword);
        secondUser.setBirthDate(secondUserBirthDate);
        secondUser.setAvatar(secondUserAvatar);
        secondUser.setBio(secondUserBio);
        secondUser.setSex(secondUserSex);
        secondUser.setLocation(secondUserLocation);
        secondUser.setRoleset(Collections.singleton(secondUserRole));

        users = Arrays.asList(firstUser, secondUser);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        Mockito.when(userRepository.existsByEmail(firstUserEmail)).thenReturn(true);
        Mockito.when(userRepository.existsByEmail(secondUserEmail)).thenReturn(true);
        Mockito.when(userRepository.existsByEmail(nonExistUserEmail)).thenReturn(false);

        Mockito.when(userRepository.findUserByEmail(firstUserEmail)).thenReturn(Optional.of(firstUser));
        Mockito.when(userRepository.findUserByEmail(secondUserEmail)).thenReturn(Optional.of(secondUser));
        Mockito.when(userRepository.findUserByEmail(nonExistUserEmail)).thenReturn(Optional.empty());

        Mockito.when(userRepository.existsById(firstUserId)).thenReturn(true);
        Mockito.when(userRepository.existsById(secondUserId)).thenReturn(true);
        Mockito.when(userRepository.existsById(nonExistUserId)).thenReturn(false);

        Mockito.when(userRepository.findById(firstUserId)).thenReturn(Optional.of(firstUser));
        Mockito.when(userRepository.findById(secondUserId)).thenReturn(Optional.of(secondUser));
        Mockito.when(userRepository.findById(nonExistUserId)).thenReturn(Optional.empty());

        Mockito.when(userRepository.save(firstUser)).thenReturn(firstUser);
        Mockito.when(userRepository.save(secondUser)).thenReturn(secondUser);

        Mockito.doNothing().when(userRepository).deleteById(firstUserId);
        Mockito.doNothing().when(userRepository).deleteById(secondUserId);

        Mockito.when(cityRepository.findById(secondUserLocationId)).thenReturn(Optional.of(secondUserLocation));
        Mockito.when(cityRepository.findById(firstUserLocationId)).thenReturn(Optional.of(firstUserLocation));

        Mockito.when(imageRepository.findById(firstUserImageId)).thenReturn(Optional.of(firstUserAvatar));
        Mockito.when(imageRepository.findById(secondUserImageId)).thenReturn(Optional.of(secondUserAvatar));


    }

    @Test
    public void whenGetAll_thenListOfUsersShouldBeReturned() {

        users = userService.getAll();
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findAll();

        assertThat(users.size()).isEqualTo(usersCount);
        assertThat(users.get(0).getFirstName()).isEqualTo(firstUserName);
        assertThat(users.get(1).getFirstName()).isEqualTo(secondUserName);

    }

    @Test
    public void whenGetById_thenUserShouldBeReturned() {

        User user = userService.get(firstUserId);
        assertThat(user.getFirstName()).isEqualTo(firstUserName);
        assertThat(user.getLastName()).isEqualTo(firstUserLastName);
        assertThat(user.getEmail()).isEqualTo(firstUserEmail);
        assertThat(user.getLogin()).isEqualTo(firstUserLogin);
        assertThat(user.getBirthDate()).isEqualTo(firstUserBirthDate);
        assertThat(user.getRoleset()).contains(firstUserRole);
        assertThat(user.getSex()).isEqualTo(firstUserSex);
        assertThat(user.getBio()).isEqualTo(firstUserBio);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenUserByIdNotExist_thenExceptionShouldBeThrown() {

        userService.get(nonExistUserId);
    }

    @Test
    public void whenGetByEmail_thenUserShouldBeReturned() {

        User user = userService.getUserByEmail(secondUserEmail);
        assertThat(user.getFirstName()).isEqualTo(secondUserName);
        assertThat(user.getLastName()).isEqualTo(secondUserLastName);
        assertThat(user.getEmail()).isEqualTo(secondUserEmail);
        assertThat(user.getLogin()).isEqualTo(secondUserLogin);
        assertThat(user.getBirthDate()).isEqualTo(secondUserBirthDate);
        assertThat(user.getRoleset()).contains(secondUserRole);
        assertThat(user.getSex()).isEqualTo(secondUserSex);
        assertThat(user.getBio()).isEqualTo(secondUserBio);

    }

    @Test(expected = DataNotFoundException.class)
    public void whenUserByEmailNotExist_thenExceptionShouldBeThrown() {

        userService.getUserByEmail(nonExistUserEmail);

    }

    @Test
    public void whenUpdateUser_thenUserShouldBeReturned() {

        assertThat(userService.update(secondUser, secondUserId).getLogin()).isEqualTo(secondUserLogin);

    }

    @Test(expected = AccessDeniedException.class)
    public void whenUpdateUserWithNonExistingId_thenExceptionShouldBeThrown() {

        userService.update(firstUser, nonExistUserId);

    }

    @Test
    public void whenDeleteUser_thenNothingShouldBeReturned() {

        userService.delete(firstUserId);

    }

    @Test(expected = DataNotFoundException.class)
    public void whenDeleteUserWithNonExistingId_thenExceptionShouldBeThrown() {

        userService.delete(nonExistUserId);

    }

    @After
    public void tearDown() {

        firstUser = null;
        secondUser = null;
        assertNull(firstUser);
        assertNull(secondUser);
    }
}
