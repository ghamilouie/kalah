package com.backbase.kalah.exception;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ExceptionResponse {
    private String message;
    private ZonedDateTime dateTime;
}
