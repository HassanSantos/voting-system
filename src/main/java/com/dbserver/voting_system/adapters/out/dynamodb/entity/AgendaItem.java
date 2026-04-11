package com.dbserver.voting_system.adapters.out.dynamodb.entity;

import java.time.Instant;

public record AgendaItem(String id, String title, String description, Instant createdAt) {
}
