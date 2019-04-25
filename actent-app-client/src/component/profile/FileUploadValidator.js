const maxImageSize = 5242880;

const imageValidator = imageSize => {
    if (imageSize < maxImageSize) {
        return true;
    } else {
        return false;
    }
};

export { imageValidator };
