package com.dbserver.voting_system.application.port.in;

import com.dbserver.voting_system.application.dto.request.CreateAgendaCommand;
import com.dbserver.voting_system.application.dto.response.AgendaResponse;

public interface CreateAgendaUseCase {

    AgendaResponse execute(CreateAgendaCommand command);
}
