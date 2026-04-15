package com.dbserver.voting_system.domain.exception;

public class InvalidVotingSessionDurationException extends RuntimeException {

    public InvalidVotingSessionDurationException(Long durationMinutes) {
        super("Voting session duration must be greater than zero. Received: " + durationMinutes);
    }
}
