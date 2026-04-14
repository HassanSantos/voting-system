package com.dbserver.voting_system.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dbserver.voting_system.application.dto.request.RegisterVoteCommand;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.CpfEligibilityPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.enums.CpfEligibilityStatus;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.DuplicateVoteException;
import com.dbserver.voting_system.domain.exception.InvalidCpfException;
import com.dbserver.voting_system.domain.exception.UnableToVoteException;
import com.dbserver.voting_system.domain.exception.VotingSessionClosedException;
import com.dbserver.voting_system.domain.exception.VotingSessionNotFoundException;
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
class RegisterVoteServiceTest {

    private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");

    @Mock
    private AgendaRepositoryPort agendaRepositoryPort;

    @Mock
    private VotingSessionRepositoryPort votingSessionRepositoryPort;

    @Mock
    private VoteRepositoryPort voteRepositoryPort;

    @Mock
    private CpfEligibilityPort cpfEligibilityPort;

    private RegisterVoteService service;

    @BeforeEach
    void setup_method_do() {
        service = new RegisterVoteService(
                agendaRepositoryPort,
                votingSessionRepositoryPort,
                voteRepositoryPort,
                cpfEligibilityPort,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
    }

    @Test
    void shouldRegisterVote_method_execute_do() {
        RegisterVoteCommand command = new RegisterVoteCommand("agenda-1", "12345678900", VoteValue.YES);
        VotingSession session = new VotingSession(
                "agenda-1",
                NOW.minusSeconds(30),
                NOW.plusSeconds(300),
                VotingSessionStatus.OPEN
        );

        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Title", "Desc", NOW.minusSeconds(60))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.of(session));
        when(cpfEligibilityPort.verify("12345678900")).thenReturn(CpfEligibilityStatus.ABLE_TO_VOTE);
        when(voteRepositoryPort.existsByAgendaIdAndCpf("agenda-1", "12345678900")).thenReturn(false);

        VoteResponse response = service.execute(command);

        assertEquals("agenda-1", response.agendaId());
        assertEquals("12345678900", response.cpf());
        assertEquals("YES", response.voteValue());
        assertEquals(NOW, response.votedAt());
        verify(voteRepositoryPort).save(any());
    }

    @Test
    void shouldThrowAgendaNotFoundException_method_execute_do() {
        RegisterVoteCommand command = new RegisterVoteCommand("agenda-1", "12345678900", VoteValue.YES);
        when(agendaRepositoryPort.findById("agenda-1")).thenReturn(Optional.empty());

        assertThrows(AgendaNotFoundException.class, () -> service.execute(command));

        verify(votingSessionRepositoryPort, never()).findByAgendaId(any());
        verify(voteRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowVotingSessionNotFoundException_method_execute_do() {
        RegisterVoteCommand command = new RegisterVoteCommand("agenda-1", "12345678900", VoteValue.YES);
        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Title", "Desc", NOW.minusSeconds(60))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.empty());

        assertThrows(VotingSessionNotFoundException.class, () -> service.execute(command));

        verify(voteRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowVotingSessionClosedException_method_execute_do() {
        RegisterVoteCommand command = new RegisterVoteCommand("agenda-1", "12345678900", VoteValue.YES);
        VotingSession closedSession = new VotingSession(
                "agenda-1",
                NOW.minusSeconds(600),
                NOW.minusSeconds(1),
                VotingSessionStatus.OPEN
        );

        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Title", "Desc", NOW.minusSeconds(60))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.of(closedSession));

        assertThrows(VotingSessionClosedException.class, () -> service.execute(command));

        verify(voteRepositoryPort, never()).existsByAgendaIdAndCpf(any(), any());
        verify(voteRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowDuplicateVoteException_method_execute_do() {
        RegisterVoteCommand command = new RegisterVoteCommand("agenda-1", "12345678900", VoteValue.YES);
        VotingSession session = new VotingSession(
                "agenda-1",
                NOW.minusSeconds(30),
                NOW.plusSeconds(300),
                VotingSessionStatus.OPEN
        );

        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Title", "Desc", NOW.minusSeconds(60))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.of(session));
        when(cpfEligibilityPort.verify("12345678900")).thenReturn(CpfEligibilityStatus.ABLE_TO_VOTE);
        when(voteRepositoryPort.existsByAgendaIdAndCpf("agenda-1", "12345678900")).thenReturn(true);

        assertThrows(DuplicateVoteException.class, () -> service.execute(command));

        verify(voteRepositoryPort, never()).save(any());
        verify(voteRepositoryPort).existsByAgendaIdAndCpf(eq("agenda-1"), eq("12345678900"));
    }

    @Test
    void shouldThrowInvalidCpfException_method_execute_do() {
        RegisterVoteCommand command = new RegisterVoteCommand("agenda-1", "12345678900", VoteValue.YES);
        VotingSession session = new VotingSession(
                "agenda-1",
                NOW.minusSeconds(30),
                NOW.plusSeconds(300),
                VotingSessionStatus.OPEN
        );

        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Title", "Desc", NOW.minusSeconds(60))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.of(session));
        when(cpfEligibilityPort.verify("12345678900")).thenThrow(new InvalidCpfException("12345678900"));

        assertThrows(InvalidCpfException.class, () -> service.execute(command));

        verify(voteRepositoryPort, never()).existsByAgendaIdAndCpf(any(), any());
        verify(voteRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowUnableToVoteException_method_execute_do() {
        RegisterVoteCommand command = new RegisterVoteCommand("agenda-1", "12345678900", VoteValue.YES);
        VotingSession session = new VotingSession(
                "agenda-1",
                NOW.minusSeconds(30),
                NOW.plusSeconds(300),
                VotingSessionStatus.OPEN
        );

        when(agendaRepositoryPort.findById("agenda-1"))
                .thenReturn(Optional.of(new Agenda("agenda-1", "Title", "Desc", NOW.minusSeconds(60))));
        when(votingSessionRepositoryPort.findByAgendaId("agenda-1")).thenReturn(Optional.of(session));
        when(cpfEligibilityPort.verify("12345678900")).thenReturn(CpfEligibilityStatus.UNABLE_TO_VOTE);

        assertThrows(UnableToVoteException.class, () -> service.execute(command));

        verify(voteRepositoryPort, never()).existsByAgendaIdAndCpf(any(), any());
        verify(voteRepositoryPort, never()).save(any());
    }
}
