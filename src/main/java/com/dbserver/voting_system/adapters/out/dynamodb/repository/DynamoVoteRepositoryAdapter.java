package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VoteItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VoteDynamoMapper;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.domain.model.Vote;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoVoteRepositoryAdapter implements VoteRepositoryPort {

    @Override
    public void save(Vote vote) {
        VoteItem item = VoteDynamoMapper.toItem(vote);
        InMemoryDynamoTables.VOTE_TABLE
                .computeIfAbsent(vote.getAgendaId(), key -> new ConcurrentHashMap<>())
                .put(vote.getAssociateId(), item);
    }

    @Override
    public boolean existsByAgendaIdAndAssociateId(String agendaId, String associateId) {
        Map<String, VoteItem> voteItems = InMemoryDynamoTables.VOTE_TABLE.get(agendaId);
        return voteItems != null && voteItems.containsKey(associateId);
    }

    @Override
    public List<Vote> findByAgendaId(String agendaId) {
        Map<String, VoteItem> voteItems = InMemoryDynamoTables.VOTE_TABLE.get(agendaId);
        if (voteItems == null) {
            return List.of();
        }

        return voteItems.values().stream()
                .map(VoteDynamoMapper::toDomain)
                .sorted(Comparator.comparing(Vote::getVotedAt))
                .toList();
    }
}
