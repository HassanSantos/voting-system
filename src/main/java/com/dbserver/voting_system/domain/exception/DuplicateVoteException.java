package com.dbserver.voting_system.domain.exception;

public class DuplicateVoteException extends RuntimeException {

    public DuplicateVoteException(String agendaId, String associateId) {
        super("Vote already registered for associate " + associateId + " in agenda " + agendaId);
    }
}
