package com.dbserver.voting_system.domain.exception;

public class AgendaNotFoundException extends RuntimeException {

    public AgendaNotFoundException(String agendaId) {
        super("Agenda not found: " + agendaId);
    }
}
