package com.bootcamp.desafioquality.controller;

public class ErrorDTO {
    public String getError() {
        return error;
    }

    public ErrorDTO setError(String error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    private String error;
    private String message;
}
