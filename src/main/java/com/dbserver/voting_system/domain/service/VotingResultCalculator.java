package com.dbserver.voting_system.domain.service;

import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingResult;
import java.util.List;

public class VotingResultCalculator {
    private static final String APPROVED = "APPROVED";
    private static final String REJECTED = "REJECTED";
    private static final String TIED = "TIED";

    public VotingResult calculate(String agendaId, List<Vote> votes) {
        long yesVotes = votes.stream().filter(vote -> vote.getValue() == VoteValue.YES).count();
        long noVotes = votes.stream().filter(vote -> vote.getValue() == VoteValue.NO).count();
        long totalVotes = yesVotes + noVotes;

        String outcome;
        if (yesVotes > noVotes) {
            outcome = APPROVED;
        } else if (noVotes > yesVotes) {
            outcome = REJECTED;
        } else {
            outcome = TIED;
        }

        return new VotingResult(agendaId, yesVotes, noVotes, totalVotes, outcome);
    }
}
