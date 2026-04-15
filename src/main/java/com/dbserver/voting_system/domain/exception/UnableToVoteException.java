package com.dbserver.voting_system.domain.exception;

import com.dbserver.voting_system.common.AppConstants;

public class UnableToVoteException extends RuntimeException {

    public UnableToVoteException(String cpf) {
        super(AppConstants.Messages.UNABLE_TO_VOTE_PREFIX + cpf);
    }
}
