package com.dbserver.voting_system.domain.exception;

import com.dbserver.voting_system.common.AppConstants;

public class DuplicateVoteException extends RuntimeException {

    public DuplicateVoteException(String agendaId, String associateId) {
        super(AppConstants.Messages.DUPLICATE_VOTE_PREFIX
                + associateId
                + AppConstants.Messages.DUPLICATE_VOTE_MIDDLE
                + agendaId);
    }
}
