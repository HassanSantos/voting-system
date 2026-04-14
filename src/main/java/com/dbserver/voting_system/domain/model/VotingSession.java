package com.dbserver.voting_system.domain.model;

import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import com.dbserver.voting_system.domain.exception.InvalidVotingSessionDurationException;
import java.time.Instant;

public class VotingSession {
    private static final String AGENDA_ID_REQUIRED = "agendaId is required";
    private static final String OPENED_AT_REQUIRED = "openedAt is required";
    private static final String ENDS_AT_REQUIRED = "endsAt is required";
    private static final String STATUS_REQUIRED = "status is required";
    private static final String ENDS_AT_AFTER_OPENED_AT = "endsAt must be after openedAt";

    private final String agendaId;
    private final Instant openedAt;
    private final Instant endsAt;
    private final VotingSessionStatus status;

    public static VotingSession open(String agendaId, Instant openedAt, Long durationMinutes) {
        long effectiveDurationMinutes = durationMinutes == null ? 1L : durationMinutes;
        if (effectiveDurationMinutes <= 0) {
            throw new InvalidVotingSessionDurationException(durationMinutes);
        }

        return new VotingSession(
                agendaId,
                openedAt,
                openedAt.plusSeconds(effectiveDurationMinutes * 60),
                VotingSessionStatus.OPEN
        );
    }

    public VotingSession(String agendaId, Instant openedAt, Instant endsAt, VotingSessionStatus status) {
        if (agendaId == null) {
            throw new IllegalArgumentException(AGENDA_ID_REQUIRED);
        }
        if (openedAt == null) {
            throw new IllegalArgumentException(OPENED_AT_REQUIRED);
        }
        if (endsAt == null) {
            throw new IllegalArgumentException(ENDS_AT_REQUIRED);
        }
        if (status == null) {
            throw new IllegalArgumentException(STATUS_REQUIRED);
        }

        if (endsAt.isBefore(openedAt) || endsAt.equals(openedAt)) {
            throw new IllegalArgumentException(ENDS_AT_AFTER_OPENED_AT);
        }

        this.agendaId = agendaId;
        this.openedAt = openedAt;
        this.endsAt = endsAt;
        this.status = status;
    }

    public boolean isOpen(Instant now) {
        return currentStatus(now) == VotingSessionStatus.OPEN;
    }

    public boolean isClosed(Instant now) {
        return currentStatus(now) == VotingSessionStatus.CLOSED;
    }

    public VotingSessionStatus currentStatus(Instant now) {
        if (status == VotingSessionStatus.CLOSED || !now.isBefore(endsAt)) {
            return VotingSessionStatus.CLOSED;
        }
        return VotingSessionStatus.OPEN;
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
