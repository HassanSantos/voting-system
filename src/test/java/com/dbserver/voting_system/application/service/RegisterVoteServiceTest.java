package com.dbserver.voting_system.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dbserver.voting_system.application.dto.request.RegisterVoteCommand;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import com.dbserver.voting_system.domain.exception.DuplicateVoteException;
import com.dbserver.voting_system.domain.exception.VotingSessionClosedException;
import com.dbserver.voting_system.domain.model.Agenda;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RegisterVoteServiceTest {

    private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");

    @org.junit.jupiter.api.Test
    void shouldRegisterVoteWhenSessionIsOpen() {
        FakeAgendaRepository agendaRepository = new FakeAgendaRepository();
        agendaRepository.save(new Agenda("agenda-1", "Pauta", "Descricao", NOW));

        FakeVotingSessionRepository sessionRepository = new FakeVotingSessionRepository();
        sessionRepository.save(new VotingSession(
                "agenda-1",
                NOW.minusSeconds(60),
                NOW.plusSeconds(300),
                VotingSessionStatus.OPEN
        ));

        FakeVoteRepository voteRepository = new FakeVoteRepository();

        RegisterVoteService service = new RegisterVoteService(
                agendaRepository,
                sessionRepository,
                voteRepository,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        VoteResponse response = service.execute(new RegisterVoteCommand("agenda-1", "associate-1", VoteValue.YES));

        assertEquals("agenda-1", response.agendaId());
        assertEquals("associate-1", response.associateId());
        assertEquals("YES", response.voteValue());
        assertEquals(1, voteRepository.findByAgendaId("agenda-1").size());
    }

    @org.junit.jupiter.api.Test
    void shouldThrowDuplicateVoteExceptionWhenAssociateAlreadyVoted() {
        FakeAgendaRepository agendaRepository = new FakeAgendaRepository();
        agendaRepository.save(new Agenda("agenda-1", "Pauta", "Descricao", NOW));

        FakeVotingSessionRepository sessionRepository = new FakeVotingSessionRepository();
        sessionRepository.save(new VotingSession(
                "agenda-1",
                NOW.minusSeconds(60),
                NOW.plusSeconds(300),
                VotingSessionStatus.OPEN
        ));

        FakeVoteRepository voteRepository = new FakeVoteRepository();
        voteRepository.save(new Vote("agenda-1", "associate-1", VoteValue.NO, NOW.minusSeconds(10)));

        RegisterVoteService service = new RegisterVoteService(
                agendaRepository,
                sessionRepository,
                voteRepository,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        assertThrows(
                DuplicateVoteException.class,
                () -> service.execute(new RegisterVoteCommand("agenda-1", "associate-1", VoteValue.YES))
        );
    }

    @org.junit.jupiter.api.Test
    void shouldThrowVotingSessionClosedExceptionWhenSessionExpired() {
        FakeAgendaRepository agendaRepository = new FakeAgendaRepository();
        agendaRepository.save(new Agenda("agenda-1", "Pauta", "Descricao", NOW));

        FakeVotingSessionRepository sessionRepository = new FakeVotingSessionRepository();
        sessionRepository.save(new VotingSession(
                "agenda-1",
                NOW.minusSeconds(600),
                NOW.minusSeconds(1),
                VotingSessionStatus.OPEN
        ));

        RegisterVoteService service = new RegisterVoteService(
                agendaRepository,
                sessionRepository,
                new FakeVoteRepository(),
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        assertThrows(
                VotingSessionClosedException.class,
                () -> service.execute(new RegisterVoteCommand("agenda-1", "associate-1", VoteValue.YES))
        );
    }

    private static class FakeAgendaRepository implements AgendaRepositoryPort {

        private final List<Agenda> agendas = new ArrayList<>();

        @Override
        public Agenda save(Agenda agenda) {
            agendas.removeIf(existing -> existing.getId().equals(agenda.getId()));
            agendas.add(agenda);
            return agenda;
        }

        @Override
        public Optional<Agenda> findById(String id) {
            return agendas.stream().filter(agenda -> agenda.getId().equals(id)).findFirst();
        }
    }

    private static class FakeVotingSessionRepository implements VotingSessionRepositoryPort {

        private final List<VotingSession> sessions = new ArrayList<>();

        @Override
        public VotingSession save(VotingSession votingSession) {
            sessions.removeIf(existing -> existing.getAgendaId().equals(votingSession.getAgendaId()));
            sessions.add(votingSession);
            return votingSession;
        }

        @Override
        public Optional<VotingSession> findByAgendaId(String agendaId) {
            return sessions.stream().filter(session -> session.getAgendaId().equals(agendaId)).findFirst();
        }
    }

    private static class FakeVoteRepository implements VoteRepositoryPort {

        private final List<Vote> votes = new ArrayList<>();

        @Override
        public void save(Vote vote) {
            votes.add(vote);
        }

        @Override
        public boolean existsByAgendaIdAndAssociateId(String agendaId, String associateId) {
            return votes.stream().anyMatch(vote ->
                    vote.getAgendaId().equals(agendaId) && vote.getAssociateId().equals(associateId)
            );
        }

        @Override
        public List<Vote> findByAgendaId(String agendaId) {
            return votes.stream().filter(vote -> vote.getAgendaId().equals(agendaId)).toList();
        }
    }
}
