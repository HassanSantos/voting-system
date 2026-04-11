package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VotingResultItem;
import com.dbserver.voting_system.domain.model.VotingResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VotingResultDynamoMapper {

    VotingResultItem toItem(VotingResult votingResult);

    VotingResult toDomain(VotingResultItem item);
}
