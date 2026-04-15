package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.AgendaItem;
import com.dbserver.voting_system.domain.model.Agenda;

public final class AgendaDynamoMapper {

    private AgendaDynamoMapper() {
    }

    public static AgendaItem toItem(Agenda agenda) {
        return new AgendaItem(agenda.getId(), agenda.getTitle(), agenda.getDescription(), agenda.getCreatedAt());
    }

    public static Agenda toDomain(AgendaItem item) {
        return new Agenda(item.id(), item.title(), item.description(), item.createdAt());
    }
}
