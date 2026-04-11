package com.dbserver.voting_system.application.port.out;

import com.dbserver.voting_system.domain.model.Agenda;
import java.util.Optional;

public interface AgendaRepositoryPort {

    Agenda save(Agenda agenda);

    Optional<Agenda> findById(String id);
}
