package com.dbserver.voting_system.config;

import com.dbserver.voting_system.common.AppConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamoDbProperties {

    private String tableName = AppConstants.Dynamo.TABLE_NAME;
}
