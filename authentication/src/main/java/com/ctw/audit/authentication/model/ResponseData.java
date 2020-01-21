package com.ctw.audit.authentication.model;

@SuppressWarnings("unused")
public interface ResponseData<T> {

    T getData();
    void setData(T data);
}
