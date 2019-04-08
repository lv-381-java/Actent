package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.validation.IncorrectStringException;
import com.softserve.actent.model.entity.Image;
import com.softserve.actent.repository.ImageRepository;
import com.softserve.actent.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public Image add(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image get(Long imageId) {

        Optional<Image> optionalImage = imageRepository.findById(imageId);

        return optionalImage.orElseThrow(() -> {
            log.error(ExceptionMessages.IMAGE_NOT_FOUND_WITH_ID + " Id: " + imageId);
            return new DataNotFoundException(ExceptionMessages.IMAGE_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND);
        });
    }

    @Override
    public Image getImageByFilePath(String filePath) {

        Optional<Image> optionalImage = imageRepository.findByFilePath(filePath);

        return optionalImage.orElseThrow(() -> {
            log.error(ExceptionMessages.IMAGE_NOT_FOUND_WITH_PATH + " Path: " + filePath);
            return new DataNotFoundException(ExceptionMessages.IMAGE_NOT_FOUND_WITH_PATH, ExceptionCode.NOT_FOUND);
        });
    }

    @Override
    @Transactional
    public List<Image> getAll() {

        return imageRepository.findAll();
    }

    @Override
    @Transactional
    public Image update(Image image, Long imageId) {

        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if (optionalImage.isPresent()) {
            image.setId(imageId);
            return imageRepository.save(image);
        } else {
            log.error(ExceptionMessages.IMAGE_NOT_FOUND_WITH_ID + " Id: " + imageId);
            throw new DataNotFoundException(ExceptionMessages.IMAGE_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void delete(Long imageId) {

        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if (optionalImage.isPresent()) {
            imageRepository.deleteById(imageId);
        } else {
            log.error(ExceptionMessages.IMAGE_NOT_FOUND_WITH_ID + " Id: " + imageId);
            throw new DataNotFoundException(ExceptionMessages.IMAGE_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND);
        }
    }
}
