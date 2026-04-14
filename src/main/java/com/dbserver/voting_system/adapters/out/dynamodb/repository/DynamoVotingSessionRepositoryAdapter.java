package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.SESSION_SK;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingSessionItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingSessionDynamoMapper;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingSessionSingleTableMapper;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Repository
@RequiredArgsConstructor
public class DynamoVotingSessionRepositoryAdapter implements VotingSessionRepositoryPort {

    private final DynamoDbTable<DynamoSingleTableRecord> votingSystemTable;
    private final VotingSessionDynamoMapper votingSessionDynamoMapper;
    private final VotingSessionSingleTableMapper votingSessionSingleTableMapper;

    @Override
    public VotingSession save(VotingSession votingSession) {
        VotingSessionItem item = votingSessionDynamoMapper.toItem(votingSession);
        votingSystemTable.putItem(votingSessionSingleTableMapper.toRecord(item));
        return votingSession;
    }

    @Override
    public Optional<VotingSession> findByAgendaId(String agendaId) {
        DynamoSingleTableRecord record = votingSystemTable.getItem(
                Key.builder()
                        .partitionValue(DynamoSingleTableKeys.agendaPk(agendaId))
                        .sortValue(SESSION_SK)
                        .build()
        );

        if (record == null) {
            return Optional.empty();
        }

        VotingSessionItem sessionItem = votingSessionSingleTableMapper.toItem(record);

        return Optional.of(votingSessionDynamoMapper.toDomain(sessionItem));
    }
}
