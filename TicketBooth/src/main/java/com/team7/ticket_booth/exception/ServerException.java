package com.team7.ticket_booth.exception;

public class ServerException extends RuntimeException {
    public ServerException(String message, Exception e) {
        super(message);
    }
}
