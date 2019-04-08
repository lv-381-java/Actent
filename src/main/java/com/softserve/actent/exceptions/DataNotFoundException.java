package com.softserve.actent.exceptions;

import com.softserve.actent.exceptions.codes.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataNotFoundException extends ActentAppException {

    public DataNotFoundException(String message, ExceptionCode exceptionCode) {
        super(message, exceptionCode);
    }
}
