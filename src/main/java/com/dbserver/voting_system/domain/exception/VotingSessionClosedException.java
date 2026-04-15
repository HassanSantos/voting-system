package com.dbserver.voting_system.domain.exception;

import com.dbserver.voting_system.common.AppConstants;

public class VotingSessionClosedException extends RuntimeException {

    public VotingSessionClosedException(String agendaId) {
        super(AppConstants.Messages.VOTING_SESSION_CLOSED_PREFIX + agendaId);
    }
}
