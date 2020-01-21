package com.ctw.audit.authentication.model;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ResponseSuccess {

    public static final ResponseSuccess FAIL = new ResponseSuccess(false);
    public static final ResponseSuccess SUCCESS = new ResponseSuccess(true);

    private Boolean success;

    public ResponseSuccess(Boolean success) {
        this.success = Boolean.TRUE.equals(success);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
