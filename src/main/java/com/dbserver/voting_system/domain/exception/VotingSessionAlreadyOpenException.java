package com.dbserver.voting_system.domain.exception;

public class VotingSessionAlreadyOpenException extends RuntimeException {

    public VotingSessionAlreadyOpenException(String agendaId) {
        super("Voting session is already open for agenda: " + agendaId);
    }
}
