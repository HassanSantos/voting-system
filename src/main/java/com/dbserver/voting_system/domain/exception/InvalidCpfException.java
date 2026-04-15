package com.dbserver.voting_system.domain.exception;

import com.dbserver.voting_system.common.AppConstants;

public class InvalidCpfException extends RuntimeException {

    public InvalidCpfException(String cpf) {
        super(AppConstants.Messages.INVALID_CPF_PREFIX + cpf);
    }
}
