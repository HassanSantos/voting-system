package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.PK;
import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.SK;

import java.util.Map;
import java.util.Optional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

public final class DynamoGetItemHelper {

    private DynamoGetItemHelper() {
    }

    public static Optional<Map<String, AttributeValue>> findByPrimaryKey(
            DynamoDbClient dynamoDbClient,
            String tableName,
            String partitionKey,
            String sortKey
    ) {
        Map<String, AttributeValue> key = Map.of(
                PK, AttributeValue.builder().s(partitionKey).build(),
                SK, AttributeValue.builder().s(sortKey).build()
        );

        Map<String, AttributeValue> item = dynamoDbClient.getItem(
                GetItemRequest.builder()
                        .tableName(tableName)
                        .key(key)
                        .consistentRead(true)
                        .build()
        ).item();

        if (item == null || item.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(item);
    }

    public static boolean existsByPrimaryKey(
            DynamoDbClient dynamoDbClient,
            String tableName,
            String partitionKey,
            String sortKey,
            String projectionExpression
    ) {
        Map<String, AttributeValue> key = Map.of(
                PK, AttributeValue.builder().s(partitionKey).build(),
                SK, AttributeValue.builder().s(sortKey).build()
        );

        Map<String, AttributeValue> item = dynamoDbClient.getItem(
                GetItemRequest.builder()
                        .tableName(tableName)
                        .key(key)
                        .consistentRead(true)
                        .projectionExpression(projectionExpression)
                        .build()
        ).item();

        return item != null && !item.isEmpty();
    }
}
