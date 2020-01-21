package com.ctw.audit.authentication.model;

@SuppressWarnings("unused")
public class ResponseStatus {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOk(){
        return "OK".equals(status);
    }
}
