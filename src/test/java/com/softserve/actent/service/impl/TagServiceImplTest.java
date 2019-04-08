package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.Tag;
import com.softserve.actent.repository.TagRepository;
import com.softserve.actent.service.TagService;
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
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagServiceImplTest {

    @Autowired
    private TagService tagService;

    @MockBean
    private TagRepository tagRepository;

    private final Long firstTagId = 1L;
    private final Long secondTagId = 2L;
    private final Long nonExistingTagId = 99L;
    private final String firstTagText = "beer";
    private final String secondTagText = "football";
    private final String nonExistingText = "sport";
    private Tag firstTag;
    private Tag secondTag;
    private List<Tag> tags;

    @Before
    public void setUp() {

        firstTag = new Tag();
        firstTag.setId(firstTagId);
        firstTag.setText(firstTagText);

        secondTag = new Tag();
        secondTag.setId(secondTagId);
        secondTag.setText(secondTagText);

        tags = Arrays.asList(firstTag, secondTag);

        Mockito.when(tagRepository.findById(firstTagId)).thenReturn(Optional.of(firstTag));
        Mockito.when(tagRepository.findById(secondTagId)).thenReturn(Optional.of(secondTag));
        Mockito.when(tagRepository.findById(nonExistingTagId)).thenReturn(Optional.empty());

        Mockito.when(tagRepository.findByText(firstTagText)).thenReturn(Optional.of(firstTag));
        Mockito.when(tagRepository.findByText(secondTagText)).thenReturn(Optional.of(secondTag));
        Mockito.when(tagRepository.findByText(nonExistingText)).thenReturn(Optional.empty());

        Mockito.when(tagRepository.findAll()).thenReturn(tags);

        Mockito.when(tagRepository.save(firstTag)).thenReturn(firstTag);
        Mockito.when(tagRepository.save(secondTag)).thenReturn(secondTag);

        Mockito.doNothing().when(tagRepository).deleteById(firstTagId);
        Mockito.doNothing().when(tagRepository).deleteById(secondTagId);
    }

    @Test
    public void whenExistingId_thenTagShouldBeFound() {

        assertThat(tagService.get(firstTagId).getText()).isEqualTo(firstTagText);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenNonExistingId_thenExceptionShouldBeThrown() {

        tagService.get(nonExistingTagId);
    }

    @Test
    public void whenExistingName_thenTagShouldBeFound() {

        assertThat(tagService.getByText(firstTagText)).isEqualTo(firstTag);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenNonExistingName_thenExceptionShouldBeThrown() {

        tagService.getByText(nonExistingText);
    }

    @Test
    public void whenGetAll_thenListOfTagsShouldBeReturned() {

        assertThat(tagService.getAll().size()).isEqualTo(tags.size());
        assertThat(tagService.getAll().get(0).getText()).isEqualTo(firstTagText);
        assertThat(tagService.getAll().get(1).getText()).isEqualTo(secondTagText);
    }

    @Test
    public void whenAddTag_thenTagShouldBeReturned() {

        assertThat(tagService.add(firstTag)).isEqualTo(firstTag);
    }

    @Test
    public void whenUpdateTagWithExistingId_thenTagShouldBeReturned() {

        assertThat(tagService.update(secondTag, secondTagId).getText()).isEqualTo(secondTagText);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenUpdateTagWithNonExistingId_thenExceptionShouldBeThrown() {

        tagService.update(firstTag, nonExistingTagId);
    }

    @Test
    public void whenDeleteTagWithExistingId_thenNothingShouldBeReturned() {

        tagService.delete(firstTagId);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenDeleteTagWithNonExistingId_thenExceptionShouldBeThrown() {

        tagService.delete(nonExistingTagId);
    }

    @After
    public void tearDown() {
        firstTag = null;
        secondTag = null;
        assertNull(firstTag);
        assertNull(secondTag);
    }
}
