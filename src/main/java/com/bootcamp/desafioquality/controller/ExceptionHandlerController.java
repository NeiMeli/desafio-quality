package com.bootcamp.desafioquality.controller;

import com.bootcamp.desafioquality.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

public interface ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    default ResponseEntity<ErrorDTO> handleException(Exception e) {
        final HttpStatus status;
        if (e instanceof MethodArgumentNotValidException) {
            return handleArgumentNotValidException((MethodArgumentNotValidException) e);
        }
        if (e instanceof BadRequestException) status = HttpStatus.BAD_REQUEST;
        else status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getClass().getSimpleName());
        errorDTO.setMessage(e.getMessage());
        return new ResponseEntity<>(errorDTO, status);
    }

    default ResponseEntity<ErrorDTO> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(String.format("%s: %s", fieldName, errorMessage));
        });
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(String.join(" - ", errors));
        errorDTO.setError(ex.getClass().getSimpleName());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
