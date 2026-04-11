package com.dbserver.voting_system.domain.model;

public class VotingResult {

    private final String agendaId;
    private final long yesVotes;
    private final long noVotes;
    private final long totalVotes;
    private final String outcome;

    public VotingResult(String agendaId, long yesVotes, long noVotes, long totalVotes, String outcome) {
        this.agendaId = agendaId;
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
        this.totalVotes = totalVotes;
        this.outcome = outcome;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public long getYesVotes() {
        return yesVotes;
    }

    public long getNoVotes() {
        return noVotes;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public String getOutcome() {
        return outcome;
    }
}
