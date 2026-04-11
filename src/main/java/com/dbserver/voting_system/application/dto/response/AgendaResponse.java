package com.dbserver.voting_system.application.dto.response;

import java.time.Instant;

public record AgendaResponse(String id, String title, String description, Instant createdAt) {
}
