package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.RESULT_SK;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingResultItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingResultDynamoMapper;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingResultSingleTableMapper;
import com.dbserver.voting_system.application.port.out.VotingResultRepositoryPort;
import com.dbserver.voting_system.domain.model.VotingResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Repository
@RequiredArgsConstructor
public class DynamoVotingResultRepositoryAdapter implements VotingResultRepositoryPort {

    private final DynamoDbTable<DynamoSingleTableRecord> votingSystemTable;
    private final VotingResultDynamoMapper votingResultDynamoMapper;
    private final VotingResultSingleTableMapper votingResultSingleTableMapper;

    @Override
    public void save(VotingResult votingResult) {
        VotingResultItem item = votingResultDynamoMapper.toItem(votingResult);
        votingSystemTable.putItem(votingResultSingleTableMapper.toRecord(item));
    }

    @Override
    public Optional<VotingResult> findByAgendaId(String agendaId) {
        DynamoSingleTableRecord record = votingSystemTable.getItem(
                Key.builder()
                        .partitionValue(DynamoSingleTableKeys.agendaPk(agendaId))
                        .sortValue(RESULT_SK)
                        .build()
        );

        if (record == null) {
            return Optional.empty();
        }

        VotingResultItem resultItem = votingResultSingleTableMapper.toItem(record);

        return Optional.of(votingResultDynamoMapper.toDomain(resultItem));
    }
}
