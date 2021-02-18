package com.bootcamp.desafioquality.controller;

import com.bootcamp.desafioquality.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    default ResponseEntity<ErrorDTO> handleException(Exception e) {
        final HttpStatus status;
        if (e instanceof BadRequestException) status = HttpStatus.BAD_REQUEST;
        else status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getClass().getSimpleName());
        errorDTO.setMessage(e.getMessage());
        return new ResponseEntity<>(errorDTO, status);
    }
}
