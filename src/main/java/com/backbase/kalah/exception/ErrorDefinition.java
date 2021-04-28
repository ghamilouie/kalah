package com.backbase.kalah.exception;


public enum ErrorDefinition implements ErrorCode {

    GAME_NOT_FOUND(404, "Game not found"),
    GAME_IS_OVER(400, "The Game is Over"),
    OPPONENT_TURN(400, "It is opponent's turn"),
    INVALID_PITID(400, "Pit number is invalid"),
    EMPTY_PIT_SELECTED(400, "The selected Pit is empty");

    private final int httpCode;
    private final String message;

    ErrorDefinition(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

