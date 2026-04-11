package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingSessionItem;
import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import com.dbserver.voting_system.domain.model.VotingSession;

public final class VotingSessionDynamoMapper {

    private VotingSessionDynamoMapper() {
    }

    public static VotingSessionItem toItem(VotingSession votingSession) {
        return new VotingSessionItem(
                votingSession.getAgendaId(),
                votingSession.getOpenedAt(),
                votingSession.getEndsAt(),
                votingSession.getStatus().name()
        );
    }

    public static VotingSession toDomain(VotingSessionItem item) {
        return new VotingSession(
                item.agendaId(),
                item.openedAt(),
                item.endsAt(),
                VotingSessionStatus.valueOf(item.status())
        );
    }
}
