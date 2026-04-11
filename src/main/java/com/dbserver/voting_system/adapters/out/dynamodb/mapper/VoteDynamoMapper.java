package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VoteItem;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.model.Vote;

public final class VoteDynamoMapper {

    private VoteDynamoMapper() {
    }

    public static VoteItem toItem(Vote vote) {
        return new VoteItem(vote.getAgendaId(), vote.getAssociateId(), vote.getValue().name(), vote.getVotedAt());
    }

    public static Vote toDomain(VoteItem item) {
        return new Vote(
                item.agendaId(),
                item.associateId(),
                VoteValue.valueOf(item.voteValue()),
                item.votedAt()
        );
    }
}
