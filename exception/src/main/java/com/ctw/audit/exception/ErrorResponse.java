package com.ctw.audit.exception;

import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
class ErrorResponse {
    private Date timestamp;
    private Integer status;
    private String error;
    private List<String> codes;
    private List<FieldError> errors;
    private String message;
    private String path;

    ErrorResponse() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", codes=" + codes +
                ", errors=" + errors +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
