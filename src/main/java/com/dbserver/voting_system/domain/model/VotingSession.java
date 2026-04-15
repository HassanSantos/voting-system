package com.dbserver.voting_system.domain.model;

import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import java.time.Instant;
import java.util.Objects;

public class VotingSession {

    private final String agendaId;
    private final Instant openedAt;
    private final Instant endsAt;
    private final VotingSessionStatus status;

    public VotingSession(String agendaId, Instant openedAt, Instant endsAt, VotingSessionStatus status) {
        this.agendaId = Objects.requireNonNull(agendaId, "agendaId is required");
        this.openedAt = Objects.requireNonNull(openedAt, "openedAt is required");
        this.endsAt = Objects.requireNonNull(endsAt, "endsAt is required");
        this.status = Objects.requireNonNull(status, "status is required");

        if (endsAt.isBefore(openedAt) || endsAt.equals(openedAt)) {
            throw new IllegalArgumentException("endsAt must be after openedAt");
        }
    }

    public boolean isOpen(Instant now) {
        return status == VotingSessionStatus.OPEN && now.isBefore(endsAt);
    }

    public String getAgendaId() {
        return agendaId;
    }

    public Instant getOpenedAt() {
        return openedAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public VotingSessionStatus getStatus() {
        return status;
    }
}
