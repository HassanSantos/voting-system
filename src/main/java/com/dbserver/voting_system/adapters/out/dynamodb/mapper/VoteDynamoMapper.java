package com.dbserver.voting_system.adapters.out.dynamodb.mapper;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.VoteItem;
import com.dbserver.voting_system.common.AppConstants;
import com.dbserver.voting_system.domain.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = AppConstants.MapStruct.COMPONENT_MODEL_SPRING)
public interface VoteDynamoMapper {

    @Mapping(target = AppConstants.MapStruct.TARGET_VOTE_VALUE, source = AppConstants.MapStruct.SOURCE_VALUE)
    VoteItem toItem(Vote vote);

    @Mapping(target = AppConstants.MapStruct.SOURCE_VALUE, source = AppConstants.MapStruct.TARGET_VOTE_VALUE)
    Vote toDomain(VoteItem item);
}
