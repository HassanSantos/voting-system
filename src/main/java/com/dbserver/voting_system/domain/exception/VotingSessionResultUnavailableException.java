package com.dbserver.voting_system.domain.exception;

public class VotingSessionResultUnavailableException extends RuntimeException {

    public VotingSessionResultUnavailableException(String agendaId) {
        super("Voting result is not available while session is open for agenda: " + agendaId);
    }
}
