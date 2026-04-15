package com.dbserver.voting_system.application.dto.request;

public record OpenVotingSessionCommand(String agendaId, Long durationMinutes) {
}
