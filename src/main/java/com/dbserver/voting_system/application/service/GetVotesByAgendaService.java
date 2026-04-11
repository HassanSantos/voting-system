package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.in.GetVotesByAgendaUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.model.Vote;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetVotesByAgendaService implements GetVotesByAgendaUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final VoteRepositoryPort voteRepositoryPort;

    @Override
    public List<VoteResponse> execute(String agendaId) {
        agendaRepositoryPort.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException(agendaId));

        return voteRepositoryPort.findByAgendaId(agendaId)
                .stream()
                .map(this::toVoteResponse)
                .toList();
    }

    private VoteResponse toVoteResponse(Vote vote) {
        return new VoteResponse(
                vote.getAgendaId(),
                vote.getAssociateId(),
                vote.getValue().name(),
                vote.getVotedAt()
        );
    }
}
