package com.dbserver.voting_system.domain.service;

import com.dbserver.voting_system.common.AppConstants;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingResult;
import java.util.List;

public class VotingResultCalculator {

    public VotingResult calculate(String agendaId, List<Vote> votes) {
        long yesVotes = votes.stream().filter(vote -> vote.getValue() == VoteValue.YES).count();
        long noVotes = votes.stream().filter(vote -> vote.getValue() == VoteValue.NO).count();
        long totalVotes = yesVotes + noVotes;

        String outcome;
        if (yesVotes > noVotes) {
            outcome = AppConstants.Outcomes.APPROVED;
        } else if (noVotes > yesVotes) {
            outcome = AppConstants.Outcomes.REJECTED;
        } else {
            outcome = AppConstants.Outcomes.TIED;
        }

        return new VotingResult(agendaId, yesVotes, noVotes, totalVotes, outcome);
    }
}
