package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingResultItem;
import com.dbserver.voting_system.domain.model.VotingResult;

public final class VotingResultDynamoMapper {

    private VotingResultDynamoMapper() {
    }

    public static VotingResultItem toItem(VotingResult votingResult) {
        return new VotingResultItem(
                votingResult.getAgendaId(),
                votingResult.getYesVotes(),
                votingResult.getNoVotes(),
                votingResult.getTotalVotes(),
                votingResult.getOutcome()
        );
    }

    public static VotingResult toDomain(VotingResultItem item) {
        return new VotingResult(item.agendaId(), item.yesVotes(), item.noVotes(), item.totalVotes(), item.outcome());
    }
}
