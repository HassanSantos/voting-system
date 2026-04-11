package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.request.CreateAgendaCommand;
import com.dbserver.voting_system.application.dto.response.AgendaResponse;
import com.dbserver.voting_system.application.port.in.CreateAgendaUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.domain.model.Agenda;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public class CreateAgendaService implements CreateAgendaUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final Clock clock;

    public CreateAgendaService(AgendaRepositoryPort agendaRepositoryPort, Clock clock) {
        this.agendaRepositoryPort = agendaRepositoryPort;
        this.clock = clock;
    }

    @Override
    public AgendaResponse execute(CreateAgendaCommand command) {
        if (command.title() == null || command.title().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }

        Agenda agenda = new Agenda(
                UUID.randomUUID().toString(),
                command.title(),
                command.description(),
                Instant.now(clock)
        );

        Agenda savedAgenda = agendaRepositoryPort.save(agenda);

        return new AgendaResponse(
                savedAgenda.getId(),
                savedAgenda.getTitle(),
                savedAgenda.getDescription(),
                savedAgenda.getCreatedAt()
        );
    }
}
