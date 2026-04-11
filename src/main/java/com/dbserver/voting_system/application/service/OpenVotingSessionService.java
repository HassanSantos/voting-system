package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.request.OpenVotingSessionCommand;
import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;
import com.dbserver.voting_system.application.port.in.OpenVotingSessionUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.VotingSessionAlreadyOpenException;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OpenVotingSessionService implements OpenVotingSessionUseCase {

    private static final long DEFAULT_DURATION_MINUTES = 1L;

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final VotingSessionRepositoryPort votingSessionRepositoryPort;
    private final Clock clock;

    @Override
    public VotingSessionResponse execute(OpenVotingSessionCommand command) {
        agendaRepositoryPort.findById(command.agendaId())
                .orElseThrow(() -> new AgendaNotFoundException(command.agendaId()));

        VotingSession existingSession = votingSessionRepositoryPort
                .findByAgendaId(command.agendaId()).orElse(null);
        if (existingSession != null && existingSession.isOpen(Instant.now(clock))) {
            throw new VotingSessionAlreadyOpenException(command.agendaId());
        }

        long durationMinutes = command.durationMinutes() == null || command.durationMinutes() <= 0
                ? DEFAULT_DURATION_MINUTES
                : command.durationMinutes();

        Instant openedAt = Instant.now(clock);
        VotingSession session = new VotingSession(
                command.agendaId(),
                openedAt,
                openedAt.plusSeconds(durationMinutes * 60),
                VotingSessionStatus.OPEN
        );

        VotingSession savedSession = votingSessionRepositoryPort.save(session);

        return new VotingSessionResponse(
                savedSession.getAgendaId(),
                savedSession.getOpenedAt(),
                savedSession.getEndsAt(),
                savedSession.getStatus()
                        .name()
        );
    }
}
