package com.dbserver.voting_system.domain.model;

import java.time.Instant;

public class Agenda {
    private static final String ID_REQUIRED = "id is required";
    private static final String TITLE_REQUIRED = "title is required";
    private static final String CREATED_AT_REQUIRED = "createdAt is required";

    private final String id;
    private final String title;
    private final String description;
    private final Instant createdAt;

    public Agenda(String id, String title, String description, Instant createdAt) {
        if (id == null) {
            throw new IllegalArgumentException(ID_REQUIRED);
        }
        if (title == null) {
            throw new IllegalArgumentException(TITLE_REQUIRED);
        }
        if (createdAt == null) {
            throw new IllegalArgumentException(CREATED_AT_REQUIRED);
        }

        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
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
