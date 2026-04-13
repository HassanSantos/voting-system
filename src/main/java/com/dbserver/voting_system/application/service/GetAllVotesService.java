package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.in.GetAllVotesUseCase;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.domain.model.Vote;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllVotesService implements GetAllVotesUseCase {

    private final VoteRepositoryPort voteRepositoryPort;

    @Override
    public List<VoteResponse> execute() {
        return voteRepositoryPort.findAll()
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
