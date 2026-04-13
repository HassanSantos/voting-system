package com.dbserver.voting_system.domain.model;

import com.dbserver.voting_system.domain.enums.VoteValue;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Vote {

    private final String agendaId;
    private final String associateId;
    private final VoteValue value;
    private final Instant votedAt;

    public Vote(String agendaId, String associateId, VoteValue value, Instant votedAt) {
        this.agendaId = Objects.requireNonNull(agendaId, "agendaId is required");
        this.associateId = Objects.requireNonNull(associateId, "associateId is required");
        this.value = Objects.requireNonNull(value, "value is required");
        this.votedAt = Objects.requireNonNull(votedAt, "votedAt is required");
    }
}
