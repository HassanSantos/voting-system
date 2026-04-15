package com.dbserver.voting_system.adapters.out.dynamodb.repository;

import static com.dbserver.voting_system.adapters.out.dynamodb.repository.DynamoSingleTableKeys.VOTE_SK_PREFIX;

import com.dbserver.voting_system.adapters.out.dynamodb.entity.DynamoSingleTableRecord;
import com.dbserver.voting_system.adapters.out.dynamodb.entity.VoteItem;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VoteDynamoMapper;
import com.dbserver.voting_system.adapters.out.dynamodb.mapper.VoteSingleTableMapper;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.common.AppConstants;
import com.dbserver.voting_system.domain.exception.DuplicateVoteException;
import com.dbserver.voting_system.domain.model.Vote;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@Repository
@RequiredArgsConstructor
public class DynamoVoteRepositoryAdapter implements VoteRepositoryPort {

    private final DynamoDbTable<DynamoSingleTableRecord> votingSystemTable;
    private final VoteDynamoMapper voteDynamoMapper;
    private final VoteSingleTableMapper voteSingleTableMapper;

    @Override
    public Vote saveIfAbsent(Vote vote) {
        VoteItem item = voteDynamoMapper.toItem(vote);
        DynamoSingleTableRecord record = voteSingleTableMapper.toRecord(item);
        Expression condition = Expression.builder()
                .expression("attribute_not_exists(pk) AND attribute_not_exists(sk)")
                .build();

        try {
            votingSystemTable.putItem(PutItemEnhancedRequest.builder(DynamoSingleTableRecord.class)
                    .item(record)
                    .conditionExpression(condition)
                    .build());
            return vote;
        } catch (ConditionalCheckFailedException exception) {
            throw new DuplicateVoteException(vote.getAgendaId(), vote.getCpf());
        }
    }

    @Override
    public List<Vote> findAll() {
        Expression filterExpression = Expression.builder()
                .expression(AppConstants.Dynamo.FILTER_ENTITY_TYPE_EQUALS)
                .putExpressionValue(
                        AppConstants.Dynamo.EXPR_ENTITY_TYPE_KEY,
                        software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder()
                                .s(AppConstants.Dynamo.ENTITY_TYPE_VOTE)
                                .build()
                )
                .build();

        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression)
                .build();

        List<DynamoSingleTableRecord> rows = new ArrayList<>();
        votingSystemTable.scan(scanRequest).stream()
                .map(Page::items)
                .forEach(rows::addAll);

        return rows.stream()
                .map(voteSingleTableMapper::toItem)
                .map(voteDynamoMapper::toDomain)
                .sorted(Comparator.comparing(Vote::getVotedAt))
                .toList();
    }

    @Override
    public List<Vote> findByAgendaId(String agendaId) {
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(
                        QueryConditional.sortBeginsWith(
                                Key.builder()
                                        .partitionValue(DynamoSingleTableKeys.agendaPk(agendaId))
                                        .sortValue(VOTE_SK_PREFIX)
                                        .build()
                        )
                )
                .build();

        List<DynamoSingleTableRecord> rows = new ArrayList<>();
        votingSystemTable.query(queryRequest).stream()
                .map(Page::items)
                .forEach(rows::addAll);

        return rows.stream()
                .map(voteSingleTableMapper::toItem)
                .map(voteDynamoMapper::toDomain)
                .sorted(Comparator.comparing(Vote::getVotedAt))
                .toList();
    }
}
