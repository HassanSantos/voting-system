package com.dbserver.voting_system.domain.exception;

public class InvalidAgendaTitleException extends RuntimeException {

    public InvalidAgendaTitleException() {
        super("title is required");
    }
}
