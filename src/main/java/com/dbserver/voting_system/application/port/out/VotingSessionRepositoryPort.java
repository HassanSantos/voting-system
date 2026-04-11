package com.dbserver.voting_system.application.port.out;

import com.dbserver.voting_system.domain.model.VotingSession;
import java.util.Optional;

public interface VotingSessionRepositoryPort {

    VotingSession save(VotingSession votingSession);

    Optional<VotingSession> findByAgendaId(String agendaId);
}
