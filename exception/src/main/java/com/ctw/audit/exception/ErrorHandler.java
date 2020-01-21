package com.ctw.audit.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@SuppressWarnings("unused")
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private final static Logger logger = Logger.getLogger(ErrorHandler.class);

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException e, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponseFactory.newErrorResponse(e, request);
        logger.error("HttpClientErrorException", e);
        return new ResponseEntity<>(errorResponse, e.getStatusCode());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleBadCredentialsErrorException(AuthenticationException e, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponseFactory.newErrorResponse(e, request);
        logger.warn(e.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> accessDeniedExceptionHandler(AccessDeniedException e, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponseFactory.newErrorResponse(e, request);

        logger.warn("AccessDeniedException " + errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> expiredJwt(ExpiredJwtException e, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponseFactory.newErrorResponse(e.getMessage(), request);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e, WebRequest request) {

        final ErrorResponse errorResponse = ErrorResponseFactory.newErrorResponse(e, request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<Object> handleIllegalArgument(ApplicationException e, WebRequest request) {

        final ErrorResponse errorResponse = ErrorResponseFactory.newErrorResponse(e, request, e.getStatus());
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {

        final ErrorResponse errorResponse = ErrorResponseFactory.newErrorResponse(
                status, (BeanPropertyBindingResult) e.getBindingResult(), request);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> unhandledExceptionHandler(Exception e, WebRequest request) {

        logger.error("Unhandled Exception: ", e);
        final ErrorResponse errorResponse = ErrorResponseFactory.newErrorResponse(
                e.getLocalizedMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
