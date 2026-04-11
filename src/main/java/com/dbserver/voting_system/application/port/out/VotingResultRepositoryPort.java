package com.dbserver.voting_system.application.port.out;

import com.dbserver.voting_system.domain.model.VotingResult;
import java.util.Optional;

public interface VotingResultRepositoryPort {

    void save(VotingResult votingResult);

    Optional<VotingResult> findByAgendaId(String agendaId);
}
