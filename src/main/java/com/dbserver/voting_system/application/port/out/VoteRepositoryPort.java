package com.dbserver.voting_system.application.port.out;

import com.dbserver.voting_system.domain.model.Vote;
import java.util.List;

public interface VoteRepositoryPort {

    void save(Vote vote);

    boolean existsByAgendaIdAndAssociateId(String agendaId, String associateId);

    List<Vote> findByAgendaId(String agendaId);
}
