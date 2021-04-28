package com.backbase.kalah.exception;

import lombok.Getter;

@Getter
public class GameException extends RuntimeException {

    private ErrorCode errorCode;

    public GameException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

