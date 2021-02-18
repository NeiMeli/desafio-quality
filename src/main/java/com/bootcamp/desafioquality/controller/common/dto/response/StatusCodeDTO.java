package com.bootcamp.desafioquality.controller.common.dto.response;

public class StatusCodeDTO {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public StatusCodeDTO setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public StatusCodeDTO setMessage(String message) {
        this.message = message;
        return this;
    }
}
