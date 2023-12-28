package com.sericulture.controller;


public class Response<T> {

    private String errorCode;

    private String errorMessage;

    private T response;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public Response() {
        errorCode = "0";
        errorMessage = "Response fetched successfully";
    }

    public Response(T t) {
        this();
        response = t;
    }

}
