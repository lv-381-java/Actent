const maxImageSize = 1048576;

const imageValidator = (imageSize) => {
    if (imageSize<maxImageSize) {
        return true;
    } else {
        return false;
    }
};

export {imageValidator};