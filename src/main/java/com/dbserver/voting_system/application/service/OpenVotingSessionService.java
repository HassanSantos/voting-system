package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.request.OpenVotingSessionCommand;
import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;
import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.port.in.OpenVotingSessionUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.VotingSessionAlreadyOpenException;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenVotingSessionService implements OpenVotingSessionUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final VotingSessionRepositoryPort votingSessionRepositoryPort;
    private final Clock clock;
    private final ApplicationResponseMapper responseMapper;

    @Override
    public VotingSessionResponse execute(OpenVotingSessionCommand command) {
        agendaRepositoryPort.findById(command.agendaId())
                .orElseThrow(() -> new AgendaNotFoundException(command.agendaId()));

        VotingSession existingSession = votingSessionRepositoryPort
                .findByAgendaId(command.agendaId()).orElse(null);
        if (existingSession != null && existingSession.isOpen(Instant.now(clock))) {
            throw new VotingSessionAlreadyOpenException(command.agendaId());
        }

        Instant openedAt = Instant.now(clock);
        VotingSession session = VotingSession.open(command.agendaId(), openedAt, command.durationMinutes());

        VotingSession savedSession = votingSessionRepositoryPort.save(session);

        return responseMapper.toVotingSessionResponse(savedSession, openedAt);
    }
}
