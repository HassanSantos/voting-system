package com.dbserver.voting_system.domain.model;

import com.dbserver.voting_system.common.AppConstants;
import com.dbserver.voting_system.domain.enums.VoteValue;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Vote {

    private final String agendaId;
    private final String cpf;
    private final VoteValue value;
    private final Instant votedAt;

    public Vote(String agendaId, String cpf, VoteValue value, Instant votedAt) {
        this.agendaId = Objects.requireNonNull(agendaId, AppConstants.Messages.AGENDA_ID_REQUIRED);
        this.cpf = Objects.requireNonNull(cpf, AppConstants.Messages.CPF_REQUIRED);
        this.value = Objects.requireNonNull(value, AppConstants.Messages.VALUE_REQUIRED);
        this.votedAt = Objects.requireNonNull(votedAt, AppConstants.Messages.VOTED_AT_REQUIRED);
    }
}
