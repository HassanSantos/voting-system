package com.dbserver.voting_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

    @Bean
    DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .build();
    }
}
