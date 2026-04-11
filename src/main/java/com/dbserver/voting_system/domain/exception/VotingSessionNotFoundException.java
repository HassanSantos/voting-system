package com.dbserver.voting_system.domain.exception;

public class VotingSessionNotFoundException extends RuntimeException {

    public VotingSessionNotFoundException(String agendaId) {
        super("Voting session not found for agenda: " + agendaId);
    }
}
