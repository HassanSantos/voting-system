package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.port.in.GetVotesByAgendaUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetVotesByAgendaService implements GetVotesByAgendaUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final VoteRepositoryPort voteRepositoryPort;
    private final ApplicationResponseMapper responseMapper;

    @Override
    public List<VoteResponse> execute(String agendaId) {
        agendaRepositoryPort.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException(agendaId));

        return voteRepositoryPort.findByAgendaId(agendaId)
                .stream()
                .map(responseMapper::toVoteResponse)
                .toList();
    }
}
