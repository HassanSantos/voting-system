package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingSessionItem;
import com.dbserver.voting_system.common.AppConstants.Dynamo;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class VotingSessionSingleTableMapper {

    private static String agendaPk(String agendaId) {
        return Dynamo.AGENDA_PK_PREFIX + agendaId;
    }

    public DynamoSingleTableRecord toRecord(VotingSessionItem item) {
        DynamoSingleTableRecord record = new DynamoSingleTableRecord();
        record.setPk(agendaPk(item.agendaId()));
        record.setSk(Dynamo.SESSION_SK);
        record.setEntityType(Dynamo.ENTITY_TYPE_VOTING_SESSION);
        record.setAgendaId(item.agendaId());
        record.setOpenedAt(item.openedAt().toString());
        record.setEndsAt(item.endsAt().toString());
        record.setStatus(item.status());
        return record;
    }

    public VotingSessionItem toItem(DynamoSingleTableRecord record) {
        return new VotingSessionItem(
                record.getAgendaId(),
                Instant.parse(record.getOpenedAt()),
                Instant.parse(record.getEndsAt()),
                record.getStatus()
        );
    }
}
