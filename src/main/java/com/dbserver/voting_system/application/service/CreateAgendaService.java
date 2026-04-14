package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.request.CreateAgendaCommand;
import com.dbserver.voting_system.application.dto.response.AgendaResponse;
import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.port.in.CreateAgendaUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.domain.exception.InvalidAgendaTitleException;
import com.dbserver.voting_system.domain.model.Agenda;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAgendaService implements CreateAgendaUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final Clock clock;
    private final ApplicationResponseMapper responseMapper;

    @Override
    public AgendaResponse execute(CreateAgendaCommand command) {
        if (command.title() == null || command.title().isBlank()) {
            throw new InvalidAgendaTitleException();
        }

        Agenda agenda = new Agenda(
                UUID.randomUUID().toString(),
                command.title(),
                command.description(),
                Instant.now(clock)
        );

        Agenda savedAgenda = agendaRepositoryPort.save(agenda);

        return responseMapper.toAgendaResponse(savedAgenda);
    }
}
