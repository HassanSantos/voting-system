package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.response.VotingResultResponse;
import com.dbserver.voting_system.application.port.in.GetVotingResultUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingResultRepositoryPort;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingResult;
import com.dbserver.voting_system.domain.service.VotingResultCalculator;
import java.util.List;

public class GetVotingResultService implements GetVotingResultUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final VoteRepositoryPort voteRepositoryPort;
    private final VotingResultRepositoryPort votingResultRepositoryPort;
    private final VotingResultCalculator votingResultCalculator;

    public GetVotingResultService(
            AgendaRepositoryPort agendaRepositoryPort,
            VoteRepositoryPort voteRepositoryPort,
            VotingResultRepositoryPort votingResultRepositoryPort,
            VotingResultCalculator votingResultCalculator
    ) {
        this.agendaRepositoryPort = agendaRepositoryPort;
        this.voteRepositoryPort = voteRepositoryPort;
        this.votingResultRepositoryPort = votingResultRepositoryPort;
        this.votingResultCalculator = votingResultCalculator;
    }

    @Override
    public VotingResultResponse execute(String agendaId) {
        agendaRepositoryPort.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException(agendaId));

        List<Vote> votes = voteRepositoryPort.findByAgendaId(agendaId);
        VotingResult result = votingResultCalculator.calculate(agendaId, votes);
        votingResultRepositoryPort.save(result);

        return new VotingResultResponse(
                result.getAgendaId(),
                result.getYesVotes(),
                result.getNoVotes(),
                result.getTotalVotes(),
                result.getOutcome()
        );
    }
}
