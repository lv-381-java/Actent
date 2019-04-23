package com.softserve.actent.constant;

public class ExceptionMessages {

    public static final String EVENT_CAN_NOT_BE_NULL = "Event can not be null";
    public static final String EVENT_CREATOR_CAN_NOT_BE_NULL = "Event creator can not be null";
    public static final String EVENT_ADDRESS_CAN_NOT_BE_NULL = "Event address can not be null";
    public static final String EVENT_CATEGORY_CAN_NOT_BE_NULL = "Event category can not be null";
    public static final String EVENT_ACCESS_TYPE_CAN_NOT_BE_NULL = "Event access type can not be null";
    public static final String USER_CAN_NOT_ASSIGNED_TWICE = "User can not assigned twice";
    public static final String USER_CAN_NOT_BE_NULL = "User can not be null";
    public static final String EVENT_USER_BY_THIS_ID_IS_NOT_FOUND = "Event user by this id is not found";
    public static final String ID_CAN_NOT_BE_NULL = "Id can not be null";
    public static final String EVENT_LIST_IS_NULL = "Event list is null.";
    public static final String LOCATION_CAN_NO_BE_NULL = "Location can not be null.";

    public static final String EVENT_BY_THIS_ID_IS_NOT_FOUND = "Event by this id is not found";
    public static final String EVENT_BY_THIS_TITLE_IS_NOT_FOUND = "Event by this title is not found";

    public static final String EQUIPMENT_BY_THIS_ID_IS_NOT_FOUND = "Equipment by this id is not found";
    public static final String EQUIPMENTS_ARE_NOT_FOUND = "Equipments are not found";

    public static final String USER_BY_THIS_ID_IS_NOT_FOUND = "User by this id is not found";
    public static final String USER_BY_THIS_EMAIL_IS_NOT_FOUND = "User by this email is not found";
    public static final String USER_BY_THIS_LOGIN_ALREADY_EXIST = "User by this login already exist";
    public static final String USER_BY_THIS_LOGIN_IS_NOT_FOUND = "User by this login is not found.";

    public static final String NO_SUBCATEGORIES_FOUND = "There is no subcategories in parent category you are looking for";
    public static final String CATEGORY_IS_NOT_FOUND = "Category with this id isn't exist";

    public static final String REVIEW_NO_ID = "You need to provide review id.";
    public static final String REVIEW_NO_TEXT = "You need to provide review text.";
    public static final String REVIEW_NO_SCORE = "You need to provide review score.";
    public static final String REVIEW_BAD_SCORE = "Review score must be in range from 1 to 5.";
    public static final String REVIEW_NOT_FOUND_WITH_ID = "Review with requested id was not found.";
    public static final String REVIEW_INAPPROPRIATE_ID = "Review id should be number greater than zero.";

    public static final String IMAGE_NO_ID = "You need to provide image id.";
    public static final String IMAGE_NO_FILEPATH = "You need to provide image url.";
    public static final String IMAGE_NOT_FOUND_WITH_ID = "Image with requested id was not found.";
    public static final String IMAGE_NOT_FOUND_WITH_PATH = "Image with requested path was not found.";
    public static final String IMAGE_INNAPPROPRIATE_ID = "Image id should be number greater than zero.";

    public static final String TAG_NO_ID = "You need to provide tag id.";
    public static final String TAG_NO_TEXT = "You need to provide tag text.";
    public static final String TAG_TOO_SHORT_TEXT = "Tag must be at least three symbols long.";
    public static final String TAG_NOT_FOUND_WITH_ID = "Tag with requested id was not found.";
    public static final String TAG_NOT_FOUND_WITH_TEXT = "Tag with requested text was not found.";
    public static final String TAG_INNAPPROPRIATE_ID = "Tag id should be number greater than zero.";

    public static final String CHAT_BY_THIS_ID_IS_NOT_FOUND = "Chat by this id is not found";
    public static final String ACTIVE_BY_THIS_TYPE_IS_NOT_FOUND = "While adding chat to active. Active by this type is not found";

    public static final String YOU_CAN_NOT_CHANGE_THIS_MESSAGE = "You can't change this message";
    public static final String MESSAGE_NOT_FOUND = "Message not found";

    public static final String ASSIGNED_EVENT_EMPTY = "Assigned event is empty";

    public static final String EMAIL_ALREADY_USED = "There is a user with such email!";
    public static final String USER_NOT_REGISTRED = "User not registered!";

    public static final String LOCATION_NOT_FOUND = "Location with this id not found";
    public static final String JSON_ERROR = "Error processing JSON results";
    public static final String API_URL_ERROR = "Error processing API URL";
    public static final String CONNECTION_ERROR = "Error connecting to API";

    public static final String LOGIN_IS_TOO_SHORT = "Login must be at least five symbols long.";
    public static final String PASSWORD_IS_TOO_SHORT = "Password must be at least six symbols long.";

    public static final String USER_BY_THIS_EMAIL_IS_EXIST = "User by this email is exist please, login again in login form";
}
