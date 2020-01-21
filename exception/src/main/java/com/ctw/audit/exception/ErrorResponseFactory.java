package com.ctw.audit.exception;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract class ErrorResponseFactory {

    private static final int CODE = 0;

    static ErrorResponse newErrorResponse(String message, WebRequest request){
        final ErrorResponse errorResponse = newErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request);

        errorResponse.setMessage(message);
        return errorResponse;
    }
    static ErrorResponse newErrorResponse(HttpStatusCodeException ex, WebRequest request){
        final ErrorResponse errorResponse = newErrorResponse(ex.getStatusCode(), request);

        errorResponse.setMessage(ex.getLocalizedMessage());
        return errorResponse;
    }

    static ErrorResponse newErrorResponse(HttpStatus status, BeanPropertyBindingResult bindingResult, WebRequest request){
        final ErrorResponse errorResponse = newErrorResponse(status, request);
        errorResponse.setMessage("Validation failed for object='"+ bindingResult.getObjectName() + "'. Error count: " + bindingResult.getFieldErrorCount());
        errorResponse.setErrors(bindingResult.getFieldErrors());
        errorResponse.setCodes(parseCodes(bindingResult.getFieldErrors()));
        return errorResponse;
    }

    private static ErrorResponse newErrorResponse(HttpStatus status, WebRequest request){
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
        return errorResponse;
    }

    private static List<String> parseCodes(List<FieldError> errors){
        if(errors == null){
            return null;
        }
        final ArrayList<String> codes = new ArrayList<>();
        for (FieldError error : errors) {
            final String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, normalizeNull(error.getObjectName()));
            codes.add(name.replace("Entity", "") + StringUtils.capitalize(error.getField()) + StringUtils.capitalize(error.getCode()));
        }
        return codes;
    }

    private static String normalizeNull(String text){
        return text != null ? text : "";
    }

    static ErrorResponse newErrorResponse(RuntimeException ex, WebRequest request, HttpStatus status){
        final ErrorResponse errorResponse = newErrorResponse(status, request);
        return normalizeResponse(errorResponse, ex.getLocalizedMessage());
    }

    static ErrorResponse newErrorResponse(AccessDeniedException ex, WebRequest request){
        final ErrorResponse errorResponse = newErrorResponse(HttpStatus.UNAUTHORIZED, request);
        errorResponse.setMessage(ex.getLocalizedMessage());
        return errorResponse;
    }

    static ErrorResponse newErrorResponse(AuthenticationException e, WebRequest request) {
        final ErrorResponse errorResponse = newErrorResponse(HttpStatus.UNAUTHORIZED, request);
        return normalizeResponse(errorResponse, e.getLocalizedMessage());
    }

    private static ErrorResponse normalizeResponse(ErrorResponse errorResponse, String localizedMessage) {
        if (localizedMessage == null) {
            return errorResponse;
        }
        final String[] messageBody = localizedMessage.split("\\|");
        errorResponse.setCodes(ImmutableList.of(messageBody[CODE]));
        errorResponse.setMessage("");

        for (int i = 1; i < messageBody.length; i++) {
            errorResponse.setMessage(errorResponse.getMessage() + messageBody[i]);
        }

        return errorResponse;
    }
}
