package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.ENTITY_TYPE;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.PK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.RESULT_SK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.SK;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingResultItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingResultDynamoMapper;
import com.dbserver.voting_system.application.port.out.VotingResultRepositoryPort;
import com.dbserver.voting_system.domain.model.VotingResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@Repository
@RequiredArgsConstructor
public class DynamoVotingResultRepositoryAdapter implements VotingResultRepositoryPort {

    private final DynamoDbClient dynamoDbClient;
    private final VotingResultDynamoMapper votingResultDynamoMapper;

    @Override
    public void save(VotingResult votingResult) {
        VotingResultItem item = votingResultDynamoMapper.toItem(votingResult);

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put(PK, AttributeValue.builder().s(DynamoSingleTableKeys.agendaPk(item.agendaId())).build());
        attributes.put(SK, AttributeValue.builder().s(RESULT_SK).build());
        attributes.put(ENTITY_TYPE, AttributeValue.builder().s("VOTING_RESULT").build());
        attributes.put("agendaId", AttributeValue.builder().s(item.agendaId()).build());
        attributes.put("yesVotes", AttributeValue.builder().n(Long.toString(item.yesVotes())).build());
        attributes.put("noVotes", AttributeValue.builder().n(Long.toString(item.noVotes())).build());
        attributes.put("totalVotes", AttributeValue.builder().n(Long.toString(item.totalVotes())).build());
        attributes.put("outcome", AttributeValue.builder().s(item.outcome()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(DynamoSingleTableKeys.TABLE_NAME)
                .item(attributes)
                .build();

        dynamoDbClient.putItem(request);
    }

    @Override
    public Optional<VotingResult> findByAgendaId(String agendaId) {
        Optional<Map<String, AttributeValue>> maybeItem = DynamoGetItemHelper.findByPrimaryKey(
                dynamoDbClient,
                DynamoSingleTableKeys.TABLE_NAME,
                DynamoSingleTableKeys.agendaPk(agendaId),
                RESULT_SK
        );
        if (maybeItem.isEmpty()) {
            return Optional.empty();
        }

        Map<String, AttributeValue> item = maybeItem.get();

        VotingResultItem resultItem = new VotingResultItem(
                item.get("agendaId").s(),
                Long.parseLong(item.get("yesVotes").n()),
                Long.parseLong(item.get("noVotes").n()),
                Long.parseLong(item.get("totalVotes").n()),
                item.get("outcome").s()
        );

        return Optional.of(votingResultDynamoMapper.toDomain(resultItem));
    }
}
