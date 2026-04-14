package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VoteItem;
import com.dbserver.voting_system.common.AppConstants.Dynamo;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class VoteSingleTableMapper {

    private static String agendaPk(String agendaId) {
        return Dynamo.AGENDA_PK_PREFIX + agendaId;
    }

    private static String voteSk(String associateId) {
        return Dynamo.VOTE_SK_PREFIX + associateId;
    }

    public DynamoSingleTableRecord toRecord(VoteItem item) {
        DynamoSingleTableRecord record = new DynamoSingleTableRecord();
        record.setPk(agendaPk(item.agendaId()));
        record.setSk(voteSk(item.associateId()));
        record.setEntityType(Dynamo.ENTITY_TYPE_VOTE);
        record.setAgendaId(item.agendaId());
        record.setAssociateId(item.associateId());
        record.setVoteValue(item.voteValue());
        record.setVotedAt(item.votedAt().toString());
        record.setGsi1pk(Dynamo.ASSOCIATE_PK_PREFIX + item.associateId());
        record.setGsi1sk(Dynamo.AGENDA_PK_PREFIX + item.agendaId() + Dynamo.HASH_SEPARATOR + item.votedAt());
        return record;
    }

    public VoteItem toItem(DynamoSingleTableRecord record) {
        return new VoteItem(
                record.getAgendaId(),
                record.getAssociateId(),
                record.getVoteValue(),
                Instant.parse(record.getVotedAt())
        );
    }
}
