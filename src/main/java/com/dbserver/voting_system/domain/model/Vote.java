package com.dbserver.voting_system.domain.model;

import com.dbserver.voting_system.domain.enums.VoteValue;
import java.time.Instant;

public class Vote {
    private static final String AGENDA_ID_REQUIRED = "agendaId is required";
    private static final String CPF_REQUIRED = "cpf is required";
    private static final String VALUE_REQUIRED = "value is required";
    private static final String VOTED_AT_REQUIRED = "votedAt is required";

    private final String agendaId;
    private final String cpf;
    private final VoteValue value;
    private final Instant votedAt;

    public Vote(String agendaId, String cpf, VoteValue value, Instant votedAt) {
        if (agendaId == null) {
            throw new IllegalArgumentException(AGENDA_ID_REQUIRED);
        }
        if (cpf == null) {
            throw new IllegalArgumentException(CPF_REQUIRED);
        }
        if (value == null) {
            throw new IllegalArgumentException(VALUE_REQUIRED);
        }
        if (votedAt == null) {
            throw new IllegalArgumentException(VOTED_AT_REQUIRED);
        }

        this.agendaId = agendaId;
        this.cpf = cpf;
        this.value = value;
        this.votedAt = votedAt;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public String getCpf() {
        return cpf;
    }

    public VoteValue getValue() {
        return value;
    }

    public Instant getVotedAt() {
        return votedAt;
    }
}
