package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.AGENDA_META_SK;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.AgendaItem;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.AgendaDynamoMapper;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.AgendaSingleTableMapper;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.domain.model.Agenda;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Repository
@RequiredArgsConstructor
public class DynamoAgendaRepositoryAdapter implements AgendaRepositoryPort {

    private final DynamoDbTable<DynamoSingleTableRecord> votingSystemTable;
    private final AgendaDynamoMapper agendaDynamoMapper;
    private final AgendaSingleTableMapper agendaSingleTableMapper;

    @Override
    public Agenda save(Agenda agenda) {
        AgendaItem item = agendaDynamoMapper.toItem(agenda);
        votingSystemTable.putItem(agendaSingleTableMapper.toRecord(item));

        return agenda;
    }

    @Override
    public Optional<Agenda> findById(String id) {
        DynamoSingleTableRecord record = votingSystemTable.getItem(
                Key.builder()
                        .partitionValue(DynamoSingleTableKeys.agendaPk(id))
                        .sortValue(AGENDA_META_SK)
                        .build()
        );

        if (record == null) {
            return Optional.empty();
        }

        AgendaItem agendaItem = agendaSingleTableMapper.toItem(record);

        return Optional.of(agendaDynamoMapper.toDomain(agendaItem));
    }
}
