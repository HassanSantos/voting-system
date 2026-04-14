package com.dbserver.voting_system.domain.model;

import com.dbserver.voting_system.common.AppConstants;
import com.dbserver.voting_system.domain.enums.VotingSessionStatus;
import java.time.Instant;
import java.util.Objects;

public class VotingSession {

    private final String agendaId;
    private final Instant openedAt;
    private final Instant endsAt;
    private final VotingSessionStatus status;

    public VotingSession(String agendaId, Instant openedAt, Instant endsAt, VotingSessionStatus status) {
        this.agendaId = Objects.requireNonNull(agendaId, AppConstants.Messages.AGENDA_ID_REQUIRED);
        this.openedAt = Objects.requireNonNull(openedAt, AppConstants.Messages.OPENED_AT_REQUIRED);
        this.endsAt = Objects.requireNonNull(endsAt, AppConstants.Messages.ENDS_AT_REQUIRED);
        this.status = Objects.requireNonNull(status, AppConstants.Messages.STATUS_REQUIRED);

        if (endsAt.isBefore(openedAt) || endsAt.equals(openedAt)) {
            throw new IllegalArgumentException(AppConstants.Messages.ENDS_AT_AFTER_OPENED_AT);
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
