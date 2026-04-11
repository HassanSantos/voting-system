package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VoteItem;
import com.dbserver.voting_system.domain.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteDynamoMapper {

    @Mapping(target = "voteValue", source = "value")
    VoteItem toItem(Vote vote);

    @Mapping(target = "value", source = "voteValue")
    Vote toDomain(VoteItem item);
}
