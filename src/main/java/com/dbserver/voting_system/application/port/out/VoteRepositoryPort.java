package com.dbserver.voting_system.application.port.out;

import com.dbserver.voting_system.domain.model.Vote;
import java.util.List;

public interface VoteRepositoryPort {

    Vote saveIfAbsent(Vote vote);

    List<Vote> findAll();

    List<Vote> findByAgendaId(String agendaId);
}
