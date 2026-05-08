package com.dbserver.voting_system.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqsProperties {

    private String voteSystemQueueUrl;
    private String endpoint = "http://localhost:4566";
    private String region = "us-east-1";
    private String accessKeyId = "test";
    private String secretAccessKey = "test";
}
