package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingResultItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingResultDynamoMapper;
import com.dbserver.voting_system.application.port.out.VotingResultRepositoryPort;
import com.dbserver.voting_system.domain.model.VotingResult;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoVotingResultRepositoryAdapter implements VotingResultRepositoryPort {

    @Override
    public void save(VotingResult votingResult) {
        VotingResultItem item = VotingResultDynamoMapper.toItem(votingResult);
        InMemoryDynamoTables.RESULT_TABLE.put(item.agendaId(), item);
    }

    @Override
    public Optional<VotingResult> findByAgendaId(String agendaId) {
        return Optional.ofNullable(InMemoryDynamoTables.RESULT_TABLE.get(agendaId))
                .map(VotingResultDynamoMapper::toDomain);
    }
}
