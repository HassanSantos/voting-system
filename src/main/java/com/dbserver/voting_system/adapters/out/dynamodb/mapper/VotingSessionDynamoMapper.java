package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingSessionItem;
import com.dbserver.voting_system.domain.model.VotingSession;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VotingSessionDynamoMapper {

    VotingSessionItem toItem(VotingSession votingSession);

    VotingSession toDomain(VotingSessionItem item);
}
