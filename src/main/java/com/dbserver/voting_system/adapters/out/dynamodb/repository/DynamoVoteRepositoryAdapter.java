package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.ENTITY_TYPE;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.PK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.SK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.VOTE_SK_PREFIX;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VoteItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VoteDynamoMapper;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.config.DynamoDbProperties;
import com.dbserver.voting_system.domain.model.Vote;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@Repository
@RequiredArgsConstructor
public class DynamoVoteRepositoryAdapter implements VoteRepositoryPort {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbProperties dynamoDbProperties;
    private final VoteDynamoMapper voteDynamoMapper;

    @Override
    public void save(Vote vote) {
        VoteItem item = voteDynamoMapper.toItem(vote);

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put(PK, AttributeValue.builder().s(DynamoSingleTableKeys.agendaPk(item.agendaId())).build());
        attributes.put(SK, AttributeValue.builder().s(DynamoSingleTableKeys.voteSk(item.associateId())).build());
        attributes.put(ENTITY_TYPE, AttributeValue.builder().s("VOTE").build());
        attributes.put("agendaId", AttributeValue.builder().s(item.agendaId()).build());
        attributes.put("associateId", AttributeValue.builder().s(item.associateId()).build());
        attributes.put("voteValue", AttributeValue.builder().s(item.voteValue()).build());
        attributes.put("votedAt", AttributeValue.builder().s(item.votedAt().toString()).build());
        attributes.put("gsi1pk", AttributeValue.builder().s("ASSOCIATE#" + item.associateId()).build());
        attributes.put("gsi1sk", AttributeValue.builder().s("AGENDA#" + item.agendaId() + "#" + item.votedAt()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(dynamoDbProperties.getTableName())
                .item(attributes)
                .conditionExpression("attribute_not_exists(pk) AND attribute_not_exists(sk)")
                .build();
        dynamoDbClient.putItem(request);
    }

    @Override
    public List<Vote> findAll() {
        List<Map<String, AttributeValue>> rows = new ArrayList<>();

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":entityType", AttributeValue.builder().s("VOTE").build());

        ScanRequest.Builder requestBuilder = ScanRequest.builder()
                .tableName(dynamoDbProperties.getTableName())
                .filterExpression("entityType = :entityType")
                .expressionAttributeValues(expressionValues)
                .consistentRead(true);

        Map<String, AttributeValue> lastEvaluatedKey = null;

        do {
            ScanRequest request = requestBuilder.exclusiveStartKey(lastEvaluatedKey).build();
            ScanResponse response = dynamoDbClient.scan(request);
            rows.addAll(response.items());
            lastEvaluatedKey = response.lastEvaluatedKey();
        } while (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty());

        return rows.stream()
                .map(this::toVoteItem)
                .map(voteDynamoMapper::toDomain)
                .sorted(Comparator.comparing(Vote::getVotedAt))
                .toList();
    }

    @Override
    public boolean existsByAgendaIdAndAssociateId(String agendaId, String associateId) {
        return DynamoGetItemHelper.existsByPrimaryKey(
                dynamoDbClient,
                dynamoDbProperties.getTableName(),
                DynamoSingleTableKeys.agendaPk(agendaId),
                DynamoSingleTableKeys.voteSk(associateId),
                PK
        );
    }

    @Override
    public List<Vote> findByAgendaId(String agendaId) {
        List<Map<String, AttributeValue>> rows = new ArrayList<>();

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":pk", AttributeValue.builder().s(DynamoSingleTableKeys.agendaPk(agendaId)).build());
        expressionValues.put(":skPrefix", AttributeValue.builder().s(VOTE_SK_PREFIX).build());

        QueryRequest.Builder requestBuilder = QueryRequest.builder()
                .tableName(dynamoDbProperties.getTableName())
                .keyConditionExpression("pk = :pk AND begins_with(sk, :skPrefix)")
                .expressionAttributeValues(expressionValues)
                .consistentRead(true);

        Map<String, AttributeValue> lastEvaluatedKey = null;

        do {
            QueryRequest request = requestBuilder.exclusiveStartKey(lastEvaluatedKey).build();
            QueryResponse response = dynamoDbClient.query(request);
            rows.addAll(response.items());
            lastEvaluatedKey = response.lastEvaluatedKey();
        } while (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty());

        return rows.stream()
                .map(this::toVoteItem)
                .map(voteDynamoMapper::toDomain)
                .sorted(Comparator.comparing(Vote::getVotedAt))
                .toList();
    }

    private VoteItem toVoteItem(Map<String, AttributeValue> attributes) {
        return new VoteItem(
                attributes.get("agendaId").s(),
                attributes.get("associateId").s(),
                attributes.get("voteValue").s(),
                Instant.parse(attributes.get("votedAt").s())
        );
    }
}
