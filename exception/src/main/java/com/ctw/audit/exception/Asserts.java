package com.ctw.audit.exception;


abstract public class Asserts {

    public static void isNull(Object obj, ErrorEnum errorEnum){
        if(obj != null) errorEnum.throwError();
    }

    public static void notNull(Object obj, ErrorEnum errorEnum){
        if(obj == null) errorEnum.throwError();
    }
    public static void notNull(Object obj, ApplicationException applicationException){
        if(obj == null) throw applicationException;
    }
    public static void isTrue(Boolean obj, ErrorEnum errorEnum){
        if(!Boolean.TRUE.equals(obj)) errorEnum.throwError();
    } 
    public static void isTrue(Boolean obj, ApplicationException applicationException){
        if(!Boolean.TRUE.equals(obj)) throw applicationException;
    }
}
