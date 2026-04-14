package com.dbserver.voting_system.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.dto.request.OpenVotingSessionCommand;
import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.VotingSessionAlreadyOpenException;
import com.dbserver.voting_system.domain.model.Agenda;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpenVotingSessionServiceTest {

    private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");

    @Mock
    private AgendaRepositoryPort agendaRepositoryPort;

    @Mock
    private VotingSessionRepositoryPort votingSessionRepositoryPort;

    private OpenVotingSessionService service;

    @BeforeEach
    void setup_method_do() {
        service = new OpenVotingSessionService(
                agendaRepositoryPort,
                votingSessionRepositoryPort,
                Clock.fixed(NOW, ZoneOffset.UTC),
                new ApplicationResponseMapper()
        );
    }

    @Test
    void shouldOpenSessionWithDefaultDuration_method_execute_do() {
        OpenVotingSessionCommand command = new OpenVotingSessionCommand("agenda-1", null);

        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Pauta", "Descricao", NOW.minusSeconds(100))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.empty());
        when(votingSessionRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        VotingSessionResponse response = service.execute(command);

        assertEquals("agenda-1", response.agendaId());
        assertEquals(NOW, response.openedAt());
        assertEquals(NOW.plusSeconds(60), response.endsAt());
        assertEquals("OPEN", response.status());
    }

    @Test
    void shouldOpenSessionWithProvidedDuration_method_execute_do() {
        OpenVotingSessionCommand command = new OpenVotingSessionCommand("agenda-1", 5L);

        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Pauta", "Descricao", NOW.minusSeconds(100))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.empty());
        when(votingSessionRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        VotingSessionResponse response = service.execute(command);

        assertEquals(NOW.plusSeconds(300), response.endsAt());
    }

    @Test
    void shouldThrowAgendaNotFoundException_method_execute_do() {
        when(agendaRepositoryPort.findById("agenda-1")).thenReturn(Optional.empty());

        assertThrows(AgendaNotFoundException.class, () -> service.execute(new OpenVotingSessionCommand("agenda-1", 1L)));

        verify(votingSessionRepositoryPort, never()).findByAgendaId(any());
        verify(votingSessionRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowVotingSessionAlreadyOpenException_method_execute_do() {
        VotingSession openSession = new VotingSession(
                "agenda-1",
                NOW.minusSeconds(10),
                NOW.plusSeconds(60),
                VotingSessionStatus.OPEN
        );

        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Pauta", "Descricao", NOW.minusSeconds(100))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.of(openSession));

        assertThrows(VotingSessionAlreadyOpenException.class, () -> service.execute(new OpenVotingSessionCommand("agenda-1", 1L)));

        verify(votingSessionRepositoryPort, never()).save(any());
    }
}
