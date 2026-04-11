package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingResultItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingResultDynamoMapper;
import com.dbserver.voting_system.application.port.out.VotingResultRepositoryPort;
import com.dbserver.voting_system.domain.model.VotingResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DynamoVotingResultRepositoryAdapter implements VotingResultRepositoryPort {

    private final VotingResultDynamoMapper votingResultDynamoMapper;

    @Override
    public void save(VotingResult votingResult) {
        VotingResultItem item = votingResultDynamoMapper.toItem(votingResult);
        InMemoryDynamoTables.RESULT_TABLE.put(item.agendaId(), item);
    }

    @Override
    public Optional<VotingResult> findByAgendaId(String agendaId) {
        return Optional.ofNullable(InMemoryDynamoTables.RESULT_TABLE.get(agendaId))
                .map(votingResultDynamoMapper::toDomain);
    }
}
