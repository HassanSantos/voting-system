package com.dbserver.voting_system.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.dynamodb")
public class DynamoDbProperties {

    private String endpoint = "http://localhost:4566";
    private String region = "us-east-1";
    private String tableName = "voting-system";
    private String accessKey = "test";
    private String secretKey = "test";
}
