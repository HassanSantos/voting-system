package com.dbserver.voting_system.domain.exception;

import com.dbserver.voting_system.common.AppConstants;

public class VotingSessionAlreadyOpenException extends RuntimeException {

    public VotingSessionAlreadyOpenException(String agendaId) {
        super(AppConstants.Messages.VOTING_SESSION_ALREADY_OPEN_PREFIX + agendaId);
    }
}
