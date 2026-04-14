package com.dbserver.voting_system.domain.exception;

import com.dbserver.voting_system.common.AppConstants;

public class AgendaNotFoundException extends RuntimeException {

    public AgendaNotFoundException(String agendaId) {
        super(AppConstants.Messages.AGENDA_NOT_FOUND_PREFIX + agendaId);
    }
}
