package com.dbserver.voting_system.domain.exception;

public class InvalidVoteValueException extends RuntimeException {

    public InvalidVoteValueException(String voteValue) {
        super("Invalid vote value: " + voteValue);
    }
}
