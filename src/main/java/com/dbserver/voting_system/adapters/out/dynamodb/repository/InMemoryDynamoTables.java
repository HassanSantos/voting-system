package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.AgendaItem;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VoteItem;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingResultItem;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingSessionItem;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class InMemoryDynamoTables {

    static final Map<String, AgendaItem> AGENDA_TABLE = new ConcurrentHashMap<>();
    static final Map<String, VotingSessionItem> SESSION_TABLE = new ConcurrentHashMap<>();
    static final Map<String, Map<String, VoteItem>> VOTE_TABLE = new ConcurrentHashMap<>();
    static final Map<String, VotingResultItem> RESULT_TABLE = new ConcurrentHashMap<>();

    private InMemoryDynamoTables() {
    }
}
