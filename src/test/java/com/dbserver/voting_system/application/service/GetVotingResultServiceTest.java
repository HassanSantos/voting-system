package com.dbserver.voting_system.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dbserver.voting_system.application.dto.response.VotingResultResponse;
import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.VotingSessionResultUnavailableException;
import com.dbserver.voting_system.domain.model.Agenda;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingSession;
import com.dbserver.voting_system.domain.service.VotingResultCalculator;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetVotingResultServiceTest {

    private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");

    @Mock
    private AgendaRepositoryPort agendaRepositoryPort;

    @Mock
    private VoteRepositoryPort voteRepositoryPort;

    @Mock
    private VotingSessionRepositoryPort votingSessionRepositoryPort;

    private GetVotingResultService service;

    @BeforeEach
    void setup_method_do() {
        service = new GetVotingResultService(
                agendaRepositoryPort,
                voteRepositoryPort,
                votingSessionRepositoryPort,
                new VotingResultCalculator(),
                new ApplicationResponseMapper(),
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
    }

    @Test
    void shouldCalculateResultWithoutPersisting_method_execute_do() {
        String agendaId = "agenda-1";

        when(agendaRepositoryPort.findById(agendaId))
                .thenReturn(Optional.of(new Agenda(agendaId, "Pauta", "Descricao", NOW.minusSeconds(60))));
        when(votingSessionRepositoryPort.findByAgendaId(agendaId))
                .thenReturn(Optional.of(new VotingSession(
                        agendaId,
                        NOW.minusSeconds(300),
                        NOW.minusSeconds(1),
                        VotingSessionStatus.OPEN
                )));
        when(voteRepositoryPort.findByAgendaId(agendaId)).thenReturn(List.of(
                new Vote(agendaId, "associate-1", VoteValue.YES, NOW),
                new Vote(agendaId, "associate-2", VoteValue.YES, NOW),
                new Vote(agendaId, "associate-3", VoteValue.NO, NOW)
        ));

        VotingResultResponse response = service.execute(agendaId);

        assertEquals(agendaId, response.agendaId());
        assertEquals(2, response.yesVotes());
        assertEquals(1, response.noVotes());
        assertEquals(3, response.totalVotes());
        assertEquals("APPROVED", response.outcome());
        verify(votingSessionRepositoryPort).findByAgendaId(agendaId);
    }

    @Test
    void shouldThrowAgendaNotFoundException_method_execute_do() {
        when(agendaRepositoryPort.findById("agenda-1")).thenReturn(Optional.empty());

        assertThrows(AgendaNotFoundException.class, () -> service.execute("agenda-1"));

        verify(voteRepositoryPort, never()).findByAgendaId(any());
        verify(votingSessionRepositoryPort, never()).findByAgendaId(any());
    }

    @Test
    void shouldThrowVotingSessionResultUnavailableExceptionWhenSessionIsOpen_method_execute_do() {
        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Pauta", "Descricao", NOW.minusSeconds(60))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1"))
                .thenReturn(Optional.of(new VotingSession(
                        "agenda-1",
                        NOW.minusSeconds(10),
                        NOW.plusSeconds(60),
                        VotingSessionStatus.OPEN
                )));

        assertThrows(VotingSessionResultUnavailableException.class, () -> service.execute("agenda-1"));

        verify(voteRepositoryPort, never()).findByAgendaId(any());
    }
}
