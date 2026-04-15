package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.port.in.GetAllVotesUseCase;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllVotesService implements GetAllVotesUseCase {

    private final VoteRepositoryPort voteRepositoryPort;
    private final ApplicationResponseMapper responseMapper;

    @Override
    public List<VoteResponse> execute() {
        return voteRepositoryPort.findAll()
                .stream()
                .map(responseMapper::toVoteResponse)
                .toList();
    }
}
