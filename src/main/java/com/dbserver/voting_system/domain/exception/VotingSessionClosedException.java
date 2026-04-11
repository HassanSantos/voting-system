package com.dbserver.voting_system.domain.exception;

public class VotingSessionClosedException extends RuntimeException {

    public VotingSessionClosedException(String agendaId) {
        super("Voting session is closed for agenda: " + agendaId);
    }
}
