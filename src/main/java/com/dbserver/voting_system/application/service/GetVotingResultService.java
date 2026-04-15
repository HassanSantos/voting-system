package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.response.VotingResultResponse;
import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.port.in.GetVotingResultUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.VotingSessionNotFoundException;
import com.dbserver.voting_system.domain.exception.VotingSessionResultUnavailableException;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingResult;
import com.dbserver.voting_system.domain.service.VotingResultCalculator;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetVotingResultService implements GetVotingResultUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final VoteRepositoryPort voteRepositoryPort;
    private final VotingSessionRepositoryPort votingSessionRepositoryPort;
    private final VotingResultCalculator votingResultCalculator;
    private final ApplicationResponseMapper responseMapper;
    private final Clock clock;

    @Override
    public VotingResultResponse execute(String agendaId) {
        agendaRepositoryPort.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException(agendaId));

        Instant now = Instant.now(clock);
        if (votingSessionRepositoryPort.findByAgendaId(agendaId)
                .orElseThrow(() -> new VotingSessionNotFoundException(agendaId))
                .isOpen(now)) {
            throw new VotingSessionResultUnavailableException(agendaId);
        }

        List<Vote> votes = voteRepositoryPort.findByAgendaId(agendaId);
        VotingResult result = votingResultCalculator.calculate(agendaId, votes);
        return responseMapper.toVotingResultResponse(result);
    }
}
