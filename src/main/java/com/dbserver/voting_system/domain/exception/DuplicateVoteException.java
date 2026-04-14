package com.dbserver.voting_system.domain.exception;

import com.dbserver.voting_system.common.AppConstants;

public class DuplicateVoteException extends RuntimeException {

    public DuplicateVoteException(String agendaId, String cpf) {
        super(AppConstants.Messages.DUPLICATE_VOTE_PREFIX
                + cpf
                + AppConstants.Messages.DUPLICATE_VOTE_MIDDLE
                + agendaId);
    }
}
