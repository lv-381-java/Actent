package com.softserve.actent.service;

import com.softserve.actent.model.entity.Image;

import java.util.List;

public interface ImageService extends BaseCrudService<Image> {

    Image getImageByFilePath(String filePath);

}
