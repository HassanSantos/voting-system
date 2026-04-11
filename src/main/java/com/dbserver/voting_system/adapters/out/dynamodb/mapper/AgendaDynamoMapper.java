package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.AgendaItem;
import com.dbserver.voting_system.domain.model.Agenda;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgendaDynamoMapper {

    AgendaItem toItem(Agenda agenda);

    Agenda toDomain(AgendaItem item);
}
