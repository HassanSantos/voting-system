package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingSessionItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingSessionDynamoMapper;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoVotingSessionRepositoryAdapter implements VotingSessionRepositoryPort {

    @Override
    public VotingSession save(VotingSession votingSession) {
        VotingSessionItem item = VotingSessionDynamoMapper.toItem(votingSession);
        InMemoryDynamoTables.SESSION_TABLE.put(item.agendaId(), item);
        return votingSession;
    }

    @Override
    public Optional<VotingSession> findByAgendaId(String agendaId) {
        return Optional.ofNullable(InMemoryDynamoTables.SESSION_TABLE.get(agendaId))
                .map(VotingSessionDynamoMapper::toDomain);
    }
}
