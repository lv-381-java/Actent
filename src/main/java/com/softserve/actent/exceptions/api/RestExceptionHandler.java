package com.softserve.actent.exceptions.api;

import com.softserve.actent.exceptions.ActentAppException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Main exception handler for all expected exceptions
     *
     * @param ex      exception
     * @param request web request
     * @return the ResponseEntity
     */
    @ExceptionHandler(ActentAppException.class)
    protected ResponseEntity<Object> handleCustomRuntimeException(ActentAppException ex,
                                                                  WebRequest request) {
        ApiError apiError = new ApiError();
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            apiError.setStatus(responseStatus.code());
        }
        log.error(ex.getMessage(), ex);

        apiError.setDebugMessage(ex.getMessage());
        apiError.setExceptionCode(ex.getExceptionCode().exceptionCode);

        return buildResponseEntity(apiError);

    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorText = ex.getParameterName() + " parameter is missing";
        log.error(errorText);
        log.error(ex.getMessage());
        return buildResponseEntity(
                new ApiError(BAD_REQUEST, ExceptionCode.MISSING_SERVLET_REQUEST_PARAMETER.exceptionCode, errorText));
    }

    /**
     * Handles org.hibernate.exception.ConstraintViolationException. Happens when you try add to
     * database duplicate value or not existed value.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintFails(ConstraintViolationException ex) {
        ApiError apiError = new ApiError();
        apiError.setStatus(BAD_REQUEST);
        apiError.setExceptionCode(ExceptionCode.DUPLICATE_VALUE.exceptionCode);
        apiError.setDebugMessage("Cannot add or update row: duplicated value");

        log.error(ex.getMessage());

        return buildResponseEntity(apiError);
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Validated validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Validated validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError();
        apiError.setStatus(BAD_REQUEST);
        apiError.setDebugMessage("Validation error");
        apiError.setExceptionCode(ExceptionCode.VALIDATION_FAILED.exceptionCode);
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());

        log.error("Validation error in handleMethodArgumentNotValid exception handler");
        log.error(ex.getMessage());

        return buildResponseEntity(apiError);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails(e. g. on parameter).
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        ApiError apiError = new ApiError();
        apiError.setStatus(BAD_REQUEST);
        apiError.setExceptionCode(ExceptionCode.VALIDATION_FAILED.exceptionCode);
        apiError.setDebugMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());

        log.error("Validation error in handleConstraintViolation exception handler");
        log.error(ex.getMessage());

        return buildResponseEntity(apiError);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatus(BAD_REQUEST);
        apiError.setDebugMessage("Malformed JSON request");
        apiError.setExceptionCode(ExceptionCode.JSON_IS_MALFORMED.exceptionCode);

        log.error("Malformed JSON request error in handleHttpMessageNotReadable exception handler");
        log.error(ex.getMessage());

        return buildResponseEntity(apiError);
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex      NoHandlerFoundException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError(BAD_REQUEST, ExceptionCode.NO_EXCEPTION_HANDLER.exceptionCode,
                String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));

        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {

        apiError.setTimestamp(LocalDateTime.now());

        HashMap<String, ApiError> errorBody = new HashMap<>();
        errorBody.put("error", apiError);

        return new ResponseEntity<>(errorBody, apiError.getStatus());
    }

}
