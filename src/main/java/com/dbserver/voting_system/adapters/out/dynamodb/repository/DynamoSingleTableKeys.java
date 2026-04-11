package com.dbserver.voting_system.adapters.out.dynamodb.repository;

final class DynamoSingleTableKeys {

    static final String PK = "pk";
    static final String SK = "sk";
    static final String ENTITY_TYPE = "entityType";

    static final String AGENDA_META_SK = "META";
    static final String SESSION_SK = "SESSION";
    static final String RESULT_SK = "RESULT";
    static final String VOTE_SK_PREFIX = "VOTE#ASSOCIATE#";

    private DynamoSingleTableKeys() {
    }

    static String agendaPk(String agendaId) {
        return "AGENDA#" + agendaId;
    }

    static String voteSk(String associateId) {
        return VOTE_SK_PREFIX + associateId;
    }
}
