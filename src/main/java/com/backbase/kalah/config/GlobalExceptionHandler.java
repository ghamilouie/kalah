package com.backbase.kalah.config;

import com.backbase.kalah.exception.ExceptionResponse;
import com.backbase.kalah.exception.GameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GameException.class)
    public ResponseEntity<?> handleExceptions(GameException ex) {
        logger.debug(ex.getErrorCode().getHttpCode() + ":" + ex.getErrorCode().getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.setDateTime(ZonedDateTime.now());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getHttpCode()));
    }
}
