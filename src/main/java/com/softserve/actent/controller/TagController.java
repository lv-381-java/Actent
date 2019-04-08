package com.softserve.actent.controller;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.CreateTagDto;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.TagDto;
import com.softserve.actent.model.entity.Tag;
import com.softserve.actent.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(UrlConstants.API_V1)
@PreAuthorize("permitAll()")
public class TagController {

    private final TagService tagService;
    private final ModelMapper modelMapper;

    @Autowired
    public TagController(TagService tagService, ModelMapper modelMapper) {
        this.tagService = tagService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/tags")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addTag(@Validated @RequestBody CreateTagDto createTagDto) {

        Tag tag = modelMapper.map(createTagDto, Tag.class);
        tag = tagService.add(tag);

        return new IdDto(tag.getId());
    }

    @GetMapping(value = "/tags/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDto getTagById(@PathVariable @NotNull(message = ExceptionMessages.TAG_NO_ID)
                                 @Positive(message = ExceptionMessages.TAG_INNAPPROPRIATE_ID) Long id) {

        Tag tag = tagService.get(id);
        return modelMapper.map(tag, TagDto.class);
    }

    @GetMapping(value = "/tags")
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> getTags() {

        List<Tag> tags = tagService.getAll();

        return tags.stream()
                .map(image -> modelMapper.map(image, TagDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/tags/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public TagDto updateTagById(@Validated @RequestBody CreateTagDto tagDto,
                                @PathVariable @NotNull(message = ExceptionMessages.TAG_NO_ID)
                                @Positive(message = ExceptionMessages.TAG_INNAPPROPRIATE_ID) Long id) {

        Tag tag = tagService.update(modelMapper.map(tagDto, Tag.class), id);
        return modelMapper.map(tag, TagDto.class);
    }

    @DeleteMapping(value = "/tags/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagById(@PathVariable @NotNull(message = ExceptionMessages.TAG_NO_ID)
                                  @Positive(message = ExceptionMessages.TAG_INNAPPROPRIATE_ID) Long id) {

        tagService.delete(id);
    }
}

