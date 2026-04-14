package com.dbserver.voting_system.config;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

    @Bean
    DynamoDbProperties dynamoDbProperties() {
        return new DynamoDbProperties();
    }

    @Bean
    DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .build();
    }

    @Bean
    DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    DynamoDbTable<DynamoSingleTableRecord> votingSystemTable(
            DynamoDbEnhancedClient enhancedClient,
            DynamoDbProperties dynamoDbProperties
    ) {
        return enhancedClient.table(
                dynamoDbProperties.getTableName(),
                TableSchema.fromBean(DynamoSingleTableRecord.class)
        );
    }
}
