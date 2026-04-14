package com.dbserver.voting_system.domain.model;

import com.dbserver.voting_system.common.AppConstants;
import java.time.Instant;
import java.util.Objects;

public class Agenda {

    private final String id;
    private final String title;
    private final String description;
    private final Instant createdAt;

    public Agenda(String id, String title, String description, Instant createdAt) {
        this.id = Objects.requireNonNull(id, AppConstants.Messages.ID_REQUIRED);
        this.title = Objects.requireNonNull(title, AppConstants.Messages.TITLE_REQUIRED);
        this.description = description;
        this.createdAt = Objects.requireNonNull(createdAt, AppConstants.Messages.CREATED_AT_REQUIRED);
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
