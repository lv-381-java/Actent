package com.softserve.actent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.dto.CreateTagDto;
import com.softserve.actent.model.dto.TagDto;
import com.softserve.actent.model.entity.Tag;
import com.softserve.actent.repository.*;
import com.softserve.actent.security.config.SecurityConfiguration;
import com.softserve.actent.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: provide security beans mocks
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TagService tagService;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ChatRepository chatRepository;
    @MockBean
    private CityRepository cityRepository;
    @MockBean
    private CountryRepository countryRepository;
    @MockBean
    private EquipmentRepository equipmentRepository;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private EventUserRepository eventUserRepository;
    @MockBean
    private ImageRepository imageRepository;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private RegionRepository regionRepository;
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private SecurityConfiguration securityConfiguration;
    @MockBean
    private FilterChainProxy springSecurityFilterChain;

    private final Long firstTagId = 1L;
    private final Long secondTagId = 2L;
    private final Long nonExistingTagId = 99L;
    private final String firstTagText = "beer";
    private final String secondTagText = "football";
    private final String nonExistingText = "sport";
    private final String tooShortText = "hi";
    private Tag firstTag;
    private Tag secondTag;
    private Tag firstTagWithoutId;
    private Tag secondTagWithoutId;
    private TagDto firstTagDto;
    private TagDto secondTagDto;
    private CreateTagDto firstCreateTagDto;
    private CreateTagDto secondCreateTagDto;
    private CreateTagDto createTagDtoWithTooShortText;
    private List<Tag> tags;

    @Before
    public void setUp() {

        firstTag = new Tag();
        firstTag.setId(firstTagId);
        firstTag.setText(firstTagText);

        secondTag = new Tag();
        secondTag.setId(secondTagId);
        secondTag.setText(secondTagText);

        firstTagWithoutId = new Tag();
        firstTagWithoutId.setText(firstTagText);

        secondTagWithoutId = new Tag();
        secondTagWithoutId.setText(secondTagText);

        firstTagDto = new TagDto(firstTagId, firstTagText);
        secondTagDto = new TagDto(secondTagId, secondTagText);

        firstCreateTagDto = new CreateTagDto(firstTagText);
        secondCreateTagDto = new CreateTagDto(secondTagText);
        createTagDtoWithTooShortText = new CreateTagDto(tooShortText);

        tags = Arrays.asList(firstTag, secondTag);

        given(tagService.add(firstTagWithoutId)).willReturn(firstTag);
        given(tagService.add(secondTagWithoutId)).willReturn(secondTag);

        given(tagService.get(firstTagId)).willReturn(firstTag);
        given(tagService.get(secondTagId)).willReturn(secondTag);
        given(tagService.get(nonExistingTagId)).willThrow(new DataNotFoundException(
                ExceptionMessages.TAG_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND));

        given(tagService.getByText(firstTagText)).willReturn(firstTag);
        given(tagService.getByText(secondTagText)).willReturn(secondTag);
        given(tagService.getByText(nonExistingText)).willThrow(new DataNotFoundException(
                ExceptionMessages.TAG_NOT_FOUND_WITH_TEXT, ExceptionCode.NOT_FOUND));

        given(tagService.getAll()).willReturn(tags);

        given(tagService.update(firstTagWithoutId, firstTagId)).willReturn(firstTag);
        given(tagService.update(secondTagWithoutId, secondTagId)).willReturn(secondTag);
        given(tagService.update(firstTagWithoutId, nonExistingTagId)).willThrow(new DataNotFoundException(
                ExceptionMessages.TAG_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND));
        given(tagService.update(secondTagWithoutId, nonExistingTagId)).willThrow(new DataNotFoundException(
                ExceptionMessages.TAG_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND));

        Mockito.doNothing().when(tagService).delete(firstTagId);
        Mockito.doNothing().when(tagService).delete(secondTagId);
        Mockito.doThrow(new DataNotFoundException(ExceptionMessages.TAG_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND))
                .when(tagService).delete(nonExistingTagId);

        given(modelMapper.map(firstTag, TagDto.class)).willReturn(firstTagDto);
        given(modelMapper.map(secondTag, TagDto.class)).willReturn(secondTagDto);
        given(modelMapper.map(firstTagDto, Tag.class)).willReturn(firstTag);
        given(modelMapper.map(secondTagDto, Tag.class)).willReturn(secondTag);
        given(modelMapper.map(firstCreateTagDto, Tag.class)).willReturn(firstTagWithoutId);
        given(modelMapper.map(secondCreateTagDto, Tag.class)).willReturn(secondTagWithoutId);
    }

    @Test
    public void whenGetTagByExistingId_thenReturnTagJson() throws Exception {

        String uri = UrlConstants.API_V1 + "/tags/" + firstTagId;
        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstTagId))
                .andExpect(jsonPath("$.text").value(firstTagText));
    }

    @Test
    public void whenGetTagByNonExistingId_thenReturnErrorJson() throws Exception {

        String uri = UrlConstants.API_V1 + "/tags/" + nonExistingTagId;
        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.debugMessage").value(ExceptionMessages.TAG_NOT_FOUND_WITH_ID));
    }

    @Test
    public void whenGetAllTags_thenReturnTagsListJson() throws Exception {

        String uri = UrlConstants.API_V1 + "/tags";
        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(tags.size())))
                .andExpect(jsonPath("$[0].id").value(firstTagId))
                .andExpect(jsonPath("$[0].text").value(firstTagText))
                .andExpect(jsonPath("$[1].id").value(secondTagId))
                .andExpect(jsonPath("$[1].text").value(secondTagText));
    }

    @Test
    public void whenAddTag_thenReturnIdJson() throws Exception {

        String url = UrlConstants.API_V1 + "/tags";
        mvc.perform(post(url)
                .content(asJsonString(firstCreateTagDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(firstTagId.toString()));
    }

    @Test
    public void whenAddTagWithTooShortText_thenReturnTagJson() throws Exception {

        String url = UrlConstants.API_V1 + "/tags/";
        mvc.perform(post(url)
                .content(asJsonString(createTagDtoWithTooShortText))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.subErrors", hasSize(1)))
                .andExpect(jsonPath("$.error.subErrors[0].message").value(ExceptionMessages.TAG_TOO_SHORT_TEXT));
    }

    @Test
    public void whenUpdateTag_thenReturnTagJson() throws Exception {

        String url = UrlConstants.API_V1 + "/tags/" + secondTagId;
        mvc.perform(put(url)
                .content(asJsonString(secondCreateTagDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(secondTagId))
                .andExpect(jsonPath("$.text").value(secondTagText));
    }

    @Test
    public void whenUpdateTagWithNonExistingId_thenReturnErrorTagJson() throws Exception {

        String url = UrlConstants.API_V1 + "/tags/" + nonExistingTagId;
        mvc.perform(put(url)
                .content(asJsonString(secondCreateTagDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.debugMessage").value(ExceptionMessages.TAG_NOT_FOUND_WITH_ID));
    }

    @Test
    public void whenUpdateTagWithTooShortText_thenReturnTagJson() throws Exception {

        String url = UrlConstants.API_V1 + "/tags/" + firstTagId;
        mvc.perform(put(url)
                .content(asJsonString(createTagDtoWithTooShortText))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.subErrors", hasSize(1)))
                .andExpect(jsonPath("$.error.subErrors[0].message").value(ExceptionMessages.TAG_TOO_SHORT_TEXT));
    }

    @Test
    public void whenDeleteTag_thenReturnNoContent() throws Exception {

        String url = UrlConstants.API_V1 + "/tags/" + firstTagId;
        mvc.perform(delete(url)).andExpect(status().isNoContent());
    }

    @Test
    public void whenDeleteTagWithNonExistingId_thenReturnNoContent() throws Exception {

        String url = UrlConstants.API_V1 + "/tags/" + nonExistingTagId;
        mvc.perform(delete(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.debugMessage").value(ExceptionMessages.TAG_NOT_FOUND_WITH_ID));
    }

    @After
    public void tearDown() {
        firstTag = null;
        secondTag = null;
        firstTagDto = null;
        secondTagDto = null;
        firstCreateTagDto = null;
        secondCreateTagDto = null;
        firstTagWithoutId = null;
        secondTagWithoutId = null;
        tags = null;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
