package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.AGENDA_META_SK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.ENTITY_TYPE;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.PK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.SK;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.AgendaItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.AgendaDynamoMapper;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.config.DynamoDbProperties;
import com.dbserver.voting_system.domain.model.Agenda;
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
public class DynamoAgendaRepositoryAdapter implements AgendaRepositoryPort {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbProperties dynamoDbProperties;
    private final AgendaDynamoMapper agendaDynamoMapper;

    @Override
    public Agenda save(Agenda agenda) {
        AgendaItem item = agendaDynamoMapper.toItem(agenda);

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put(PK, AttributeValue.builder().s(DynamoSingleTableKeys.agendaPk(item.id())).build());
        attributes.put(SK, AttributeValue.builder().s(AGENDA_META_SK).build());
        attributes.put(ENTITY_TYPE, AttributeValue.builder().s("AGENDA").build());
        attributes.put("id", AttributeValue.builder().s(item.id()).build());
        attributes.put("title", AttributeValue.builder().s(item.title()).build());
        attributes.put("createdAt", AttributeValue.builder().s(item.createdAt().toString()).build());

        if (item.description() != null) {
            attributes.put("description", AttributeValue.builder().s(item.description()).build());
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName(dynamoDbProperties.getTableName())
                .item(attributes)
                .build();

        dynamoDbClient.putItem(request);

        return agenda;
    }

    @Override
    public Optional<Agenda> findById(String id) {
        Optional<Map<String, AttributeValue>> maybeItem = DynamoGetItemHelper.findByPrimaryKey(
                dynamoDbClient,
                dynamoDbProperties.getTableName(),
                DynamoSingleTableKeys.agendaPk(id),
                AGENDA_META_SK
        );
        if (maybeItem.isEmpty()) {
            return Optional.empty();
        }

        Map<String, AttributeValue> item = maybeItem.get();

        AgendaItem agendaItem = new AgendaItem(
                item.get("id").s(),
                item.get("title").s(),
                item.containsKey("description") ? item.get("description").s() : null,
                Instant.parse(item.get("createdAt").s())
        );

        return Optional.of(agendaDynamoMapper.toDomain(agendaItem));
    }
}
