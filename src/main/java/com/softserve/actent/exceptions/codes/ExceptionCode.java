package com.softserve.actent.exceptions.codes;

public enum ExceptionCode {

    MISSING_SERVLET_REQUEST_PARAMETER(9991),

    UNSUPPORTED_MEDIA_TYPE(9992),

    JSON_IS_MALFORMED(1993),

    NO_EXCEPTION_HANDLER(1994),

    VALIDATION_FAILED(1000),

    DUPLICATE_VALUE(1001),

    INCORRECT_EMAIL(1052),

    INCORRECT_STRING(1803),

    INCORRECT_ACTIVITY_TYPE(1806),

    NOT_FOUND(2010),

    CHAT_NOT_FOUND(2090),

    NOT_AUTHORIZED(2125),

    MESSAGE_NOT_FOUND(2060),

    USER_NOT_FOUND(2090),

    THE_FIELD_IS_NULL(2100);

    public final int exceptionCode;

    private ExceptionCode(int code) {
        exceptionCode = code;
    }
}
