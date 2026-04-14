package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingResultItem;
import com.dbserver.voting_system.common.AppConstants.Dynamo;
import org.springframework.stereotype.Component;

@Component
public class VotingResultSingleTableMapper {

    private static String agendaPk(String agendaId) {
        return Dynamo.AGENDA_PK_PREFIX + agendaId;
    }

    public DynamoSingleTableRecord toRecord(VotingResultItem item) {
        DynamoSingleTableRecord record = new DynamoSingleTableRecord();
        record.setPk(agendaPk(item.agendaId()));
        record.setSk(Dynamo.RESULT_SK);
        record.setEntityType(Dynamo.ENTITY_TYPE_VOTING_RESULT);
        record.setAgendaId(item.agendaId());
        record.setYesVotes(item.yesVotes());
        record.setNoVotes(item.noVotes());
        record.setTotalVotes(item.totalVotes());
        record.setOutcome(item.outcome());
        return record;
    }

    public VotingResultItem toItem(DynamoSingleTableRecord record) {
        return new VotingResultItem(
                record.getAgendaId(),
                record.getYesVotes(),
                record.getNoVotes(),
                record.getTotalVotes(),
                record.getOutcome()
        );
    }
}
