package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.AgendaItem;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import com.dbserver.voting_system.common.AppConstants.Dynamo;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class AgendaSingleTableMapper {

    private static String agendaPk(String agendaId) {
        return Dynamo.AGENDA_PK_PREFIX + agendaId;
    }

    public DynamoSingleTableRecord toRecord(AgendaItem item) {
        DynamoSingleTableRecord record = new DynamoSingleTableRecord();
        record.setPk(agendaPk(item.id()));
        record.setSk(Dynamo.AGENDA_META_SK);
        record.setEntityType(Dynamo.ENTITY_TYPE_AGENDA);
        record.setId(item.id());
        record.setTitle(item.title());
        record.setDescription(item.description());
        record.setCreatedAt(item.createdAt().toString());
        return record;
    }

    public AgendaItem toItem(DynamoSingleTableRecord record) {
        return new AgendaItem(
                record.getId(),
                record.getTitle(),
                record.getDescription(),
                Instant.parse(record.getCreatedAt())
        );
    }
}
