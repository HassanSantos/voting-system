package com.dbserver.voting_system.domain.model;

import java.time.Instant;
import java.util.Objects;

public class Agenda {

    private final String id;
    private final String title;
    private final String description;
    private final Instant createdAt;

    public Agenda(String id, String title, String description, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.title = Objects.requireNonNull(title, "title is required");
        this.description = description;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
