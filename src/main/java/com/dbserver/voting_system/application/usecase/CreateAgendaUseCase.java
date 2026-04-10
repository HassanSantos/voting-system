package com.dbserver.voting_system.application.usecase;

import com.dbserver.voting_system.application.domain.model.Agenda;

public interface CreateAgendaUseCase {

    void execute(Agenda agenda);
}
