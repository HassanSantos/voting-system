package com.dbserver.voting_system.config;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

    @Bean
    @ConfigurationProperties(prefix = "aws.dynamodb")
    DynamoDbProperties dynamoDbProperties() {
        return new DynamoDbProperties();
    }

    @Bean
    DynamoDbClient dynamoDbClient(DynamoDbProperties dynamoDbProperties) {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamoDbProperties.getEndpoint()))
                .region(Region.of(dynamoDbProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        dynamoDbProperties.getAccessKeyId(),
                        dynamoDbProperties.getSecretAccessKey()
                )))
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
