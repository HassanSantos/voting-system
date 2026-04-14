package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import com.dbserver.voting_system.common.AppConstants;

final class DynamoSingleTableKeys {

    static final String TABLE_NAME = AppConstants.Dynamo.TABLE_NAME;
    static final String PK = AppConstants.Dynamo.PK;
    static final String SK = AppConstants.Dynamo.SK;
    static final String ENTITY_TYPE = AppConstants.Dynamo.ENTITY_TYPE;

    static final String AGENDA_META_SK = AppConstants.Dynamo.AGENDA_META_SK;
    static final String SESSION_SK = AppConstants.Dynamo.SESSION_SK;
    static final String RESULT_SK = AppConstants.Dynamo.RESULT_SK;
    static final String VOTE_SK_PREFIX = AppConstants.Dynamo.VOTE_SK_PREFIX;
    private static final String AGENDA_PK_PREFIX = AppConstants.Dynamo.AGENDA_PK_PREFIX;

    private DynamoSingleTableKeys() {
    }

    static String agendaPk(String agendaId) {
        return AGENDA_PK_PREFIX + agendaId;
    }

    static String voteSk(String associateId) {
        return VOTE_SK_PREFIX + associateId;
    }
}
