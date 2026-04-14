package com.dbserver.voting_system.application.port.out;

import com.dbserver.voting_system.domain.enums.CpfEligibilityStatus;

public interface CpfEligibilityPort {

    CpfEligibilityStatus verify(String cpf);
}
