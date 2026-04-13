package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.ENTITY_TYPE;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.PK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.SESSION_SK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.SK;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingSessionItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VotingSessionDynamoMapper;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.config.DynamoDbProperties;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.time.Instant;
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
public class DynamoVotingSessionRepositoryAdapter implements VotingSessionRepositoryPort {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbProperties dynamoDbProperties;
    private final VotingSessionDynamoMapper votingSessionDynamoMapper;

    @Override
    public VotingSession save(VotingSession votingSession) {
        VotingSessionItem item = votingSessionDynamoMapper.toItem(votingSession);

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put(PK, AttributeValue.builder().s(DynamoSingleTableKeys.agendaPk(item.agendaId())).build());
        attributes.put(SK, AttributeValue.builder().s(SESSION_SK).build());
        attributes.put(ENTITY_TYPE, AttributeValue.builder().s("VOTING_SESSION").build());
        attributes.put("agendaId", AttributeValue.builder().s(item.agendaId()).build());
        attributes.put("openedAt", AttributeValue.builder().s(item.openedAt().toString()).build());
        attributes.put("endsAt", AttributeValue.builder().s(item.endsAt().toString()).build());
        attributes.put("status", AttributeValue.builder().s(item.status()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(dynamoDbProperties.getTableName())
                .item(attributes)
                .build();

        dynamoDbClient.putItem(request);
        return votingSession;
    }

    @Override
    public Optional<VotingSession> findByAgendaId(String agendaId) {
        Optional<Map<String, AttributeValue>> maybeItem = DynamoGetItemHelper.findByPrimaryKey(
                dynamoDbClient,
                dynamoDbProperties.getTableName(),
                DynamoSingleTableKeys.agendaPk(agendaId),
                SESSION_SK
        );
        if (maybeItem.isEmpty()) {
            return Optional.empty();
        }

        Map<String, AttributeValue> item = maybeItem.get();

        VotingSessionItem sessionItem = new VotingSessionItem(
                item.get("agendaId").s(),
                Instant.parse(item.get("openedAt").s()),
                Instant.parse(item.get("endsAt").s()),
                item.get("status").s()
        );

        return Optional.of(votingSessionDynamoMapper.toDomain(sessionItem));
    }
}
