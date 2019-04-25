package com.softserve.actent.controller;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.AddImageDto;
import com.softserve.actent.model.dto.ImageDto;
import com.softserve.actent.model.entity.Image;
import com.softserve.actent.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(UrlConstants.API_V1)
@PreAuthorize("permitAll()")
public class ImageController {

    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Autowired
    public ImageController(ImageService imageService, ModelMapper modelMapper) {
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin
    @PostMapping(value = "/images")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addImage(@Validated @RequestBody AddImageDto addImageDto) {

        Image image = modelMapper.map(addImageDto, Image.class);
        image = imageService.add(image);

        return new IdDto(image.getId());
    }

    @CrossOrigin
    @GetMapping(value = "/images/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ImageDto getImageById(@PathVariable @NotNull(message = ExceptionMessages.IMAGE_NO_ID)
                                 @Positive(message = ExceptionMessages.IMAGE_INNAPPROPRIATE_ID) Long id) {

        Image image = imageService.get(id);
        return modelMapper.map(image, ImageDto.class);
    }

    @CrossOrigin
    @GetMapping(value = "/images")
    @ResponseStatus(HttpStatus.OK)
    public List<ImageDto> getImages(@RequestParam(value = "url", required = false) String url) {

        if (url != null) {

            List<ImageDto> imagesDto = new ArrayList<>();

            Image image = imageService.getImageByFilePath(url);
            imagesDto.add(modelMapper.map(image, ImageDto.class));

            return imagesDto;
        } else {

            List<Image> images = imageService.getAll();

            return images.stream()
                    .map(image -> modelMapper.map(image, ImageDto.class))
                    .collect(Collectors.toList());
        }
    }

    @CrossOrigin
    @PutMapping(value = "/images/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public ImageDto updateImage(@Validated @RequestBody AddImageDto addImageDto,
                                @PathVariable @NotNull(message = ExceptionMessages.IMAGE_NO_ID)
                                @Positive(message = ExceptionMessages.IMAGE_INNAPPROPRIATE_ID) Long id) {

        Image image = imageService.update(modelMapper.map(addImageDto, Image.class), id);
        return modelMapper.map(image, ImageDto.class);
    }

    @CrossOrigin
    @DeleteMapping(value = "/images/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable @NotNull(message = ExceptionMessages.IMAGE_NO_ID)
                                @Positive(message = ExceptionMessages.IMAGE_INNAPPROPRIATE_ID) Long id) {

        imageService.delete(id);
    }
}
