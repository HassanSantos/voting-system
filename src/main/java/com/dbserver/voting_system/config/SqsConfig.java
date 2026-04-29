package com.dbserver.voting_system.config;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

    @Bean
    @ConfigurationProperties(prefix = "aws.sqs")
    SqsProperties sqsProperties() {
        return new SqsProperties();
    }

    @Bean
    SqsClient sqsClient(SqsProperties sqsProperties) {
        return SqsClient.builder()
                .endpointOverride(URI.create(sqsProperties.getEndpoint()))
                .region(Region.of(sqsProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        sqsProperties.getAccessKeyId(),
                        sqsProperties.getSecretAccessKey()
                )))
                .build();
    }
}
