package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.AgendaItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.AgendaDynamoMapper;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.domain.model.Agenda;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DynamoAgendaRepositoryAdapter implements AgendaRepositoryPort {

    @Override
    public Agenda save(Agenda agenda) {
        AgendaItem item = AgendaDynamoMapper.toItem(agenda);
        InMemoryDynamoTables.AGENDA_TABLE.put(item.id(), item);
        return agenda;
    }

    @Override
    public Optional<Agenda> findById(String id) {
        return Optional.ofNullable(InMemoryDynamoTables.AGENDA_TABLE.get(id))
                .map(AgendaDynamoMapper::toDomain);
    }
}
