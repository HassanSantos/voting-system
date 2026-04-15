package com.dbserver.voting_system.domain.exception;

import com.dbserver.voting_system.common.AppConstants;

public class VotingSessionNotFoundException extends RuntimeException {

    public VotingSessionNotFoundException(String agendaId) {
        super(AppConstants.Messages.VOTING_SESSION_NOT_FOUND_PREFIX + agendaId);
    }
}
