package com.dbserver.voting_system.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamoDbProperties {

    private String tableName = "voting-system";
}
