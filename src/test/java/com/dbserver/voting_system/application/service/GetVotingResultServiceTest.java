package com.dbserver.voting_system.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dbserver.voting_system.application.dto.response.VotingResultResponse;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingResultRepositoryPort;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.model.Agenda;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingResult;
import com.dbserver.voting_system.domain.service.VotingResultCalculator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class GetVotingResultServiceTest {

    @org.junit.jupiter.api.Test
    void shouldCalculateVotingResult() {
        FakeAgendaRepository agendaRepository = new FakeAgendaRepository();
        agendaRepository.save(new Agenda("agenda-1", "Pauta", "Descricao", Instant.now()));

        FakeVoteRepository voteRepository = new FakeVoteRepository();
        voteRepository.save(new Vote("agenda-1", "associate-1", VoteValue.YES, Instant.now()));
        voteRepository.save(new Vote("agenda-1", "associate-2", VoteValue.YES, Instant.now()));
        voteRepository.save(new Vote("agenda-1", "associate-3", VoteValue.NO, Instant.now()));

        FakeVotingResultRepository resultRepository = new FakeVotingResultRepository();

        GetVotingResultService service = new GetVotingResultService(
                agendaRepository,
                voteRepository,
                resultRepository,
                new VotingResultCalculator()
        );

        VotingResultResponse response = service.execute("agenda-1");

        assertEquals(2, response.yesVotes());
        assertEquals(1, response.noVotes());
        assertEquals(3, response.totalVotes());
        assertEquals("APPROVED", response.outcome());
        assertEquals(1, resultRepository.results.size());
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

    private static class FakeVotingResultRepository implements VotingResultRepositoryPort {

        private final List<VotingResult> results = new ArrayList<>();

        @Override
        public void save(VotingResult votingResult) {
            results.removeIf(existing -> existing.getAgendaId().equals(votingResult.getAgendaId()));
            results.add(votingResult);
        }

        @Override
        public Optional<VotingResult> findByAgendaId(String agendaId) {
            return results.stream().filter(result -> result.getAgendaId().equals(agendaId)).findFirst();
        }
    }
}
