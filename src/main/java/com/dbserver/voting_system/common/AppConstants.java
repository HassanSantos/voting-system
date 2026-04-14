package com.dbserver.voting_system.common;

public final class AppConstants {

    private AppConstants() {
    }

    public static final class Api {

        public static final String API_PREFIX = "/api/v1";
        public static final String API_PATH_PREFIX = "/api/";

        private Api() {
        }
    }

    public static final class Routes {

        public static final String AGENDAS_BASE_PATH = "/agendas";
        public static final String AGENDA_VOTES_PATH = "/{agendaId}/votes";
        public static final String VOTES_PATH = "/votes";
        public static final String AGENDA_RESULT_PATH = "/{agendaId}/result";
        public static final String AGENDA_SESSIONS_PATH = "/{agendaId}/sessions";

        private Routes() {
        }
    }

    public static final class Messages {

        public static final String ID_REQUIRED = "id is required";
        public static final String TITLE_REQUIRED = "title is required";
        public static final String CREATED_AT_REQUIRED = "createdAt is required";
        public static final String AGENDA_ID_REQUIRED = "agendaId is required";
        public static final String ASSOCIATE_ID_REQUIRED = "associateId is required";
        public static final String VALUE_REQUIRED = "value is required";
        public static final String VOTED_AT_REQUIRED = "votedAt is required";
        public static final String OPENED_AT_REQUIRED = "openedAt is required";
        public static final String ENDS_AT_REQUIRED = "endsAt is required";
        public static final String STATUS_REQUIRED = "status is required";
        public static final String ENDS_AT_AFTER_OPENED_AT = "endsAt must be after openedAt";
        public static final String AGENDA_NOT_FOUND_PREFIX = "Agenda not found: ";
        public static final String VOTING_SESSION_ALREADY_OPEN_PREFIX = "Voting session is already open for agenda: ";
        public static final String DUPLICATE_VOTE_PREFIX = "Vote already registered for associate ";
        public static final String DUPLICATE_VOTE_MIDDLE = " in agenda ";
        public static final String VOTING_SESSION_CLOSED_PREFIX = "Voting session is closed for agenda: ";
        public static final String VOTING_SESSION_NOT_FOUND_PREFIX = "Voting session not found for agenda: ";

        private Messages() {
        }
    }

    public static final class Outcomes {

        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
        public static final String TIED = "TIED";

        private Outcomes() {
        }
    }

    public static final class Dynamo {

        public static final String TABLE_NAME = "voting-system";
        public static final String PK = "pk";
        public static final String SK = "sk";
        public static final String ENTITY_TYPE = "entityType";

        public static final String AGENDA_META_SK = "META";
        public static final String SESSION_SK = "SESSION";
        public static final String RESULT_SK = "RESULT";
        public static final String VOTE_SK_PREFIX = "VOTE#ASSOCIATE#";
        public static final String AGENDA_PK_PREFIX = "AGENDA#";
        public static final String ASSOCIATE_PK_PREFIX = "ASSOCIATE#";

        public static final String ENTITY_TYPE_AGENDA = "AGENDA";
        public static final String ENTITY_TYPE_VOTE = "VOTE";
        public static final String ENTITY_TYPE_VOTING_RESULT = "VOTING_RESULT";
        public static final String ENTITY_TYPE_VOTING_SESSION = "VOTING_SESSION";

        public static final String GSI1_INDEX = "gsi1";

        public static final String HASH_SEPARATOR = "#";
        public static final String EXPR_ENTITY_TYPE_KEY = ":entityType";
        public static final String FILTER_ENTITY_TYPE_EQUALS = "entityType = :entityType";

        private Dynamo() {
        }
    }

    public static final class MapStruct {

        public static final String COMPONENT_MODEL_SPRING = "spring";
        public static final String TARGET_VOTE_VALUE = "voteValue";
        public static final String SOURCE_VALUE = "value";

        private MapStruct() {
        }
    }
}
