package com.dbserver.voting_system.adapters.out.sqs;

import com.dbserver.voting_system.adapters.out.sqs.dto.VoteMessage;
import com.dbserver.voting_system.application.port.out.VoteQueuePort;
import com.dbserver.voting_system.config.SqsProperties;
import com.dbserver.voting_system.domain.model.Vote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@RequiredArgsConstructor
public class SqsVoteQueueAdapter implements VoteQueuePort {

    private final SqsClient sqsClient;
    private final SqsProperties sqsProperties;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(Vote vote) {
        VoteMessage message = new VoteMessage(
                vote.getAgendaId(),
                vote.getCpf(),
                vote.getValue().name(),
                vote.getVotedAt()
        );

        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(sqsProperties.getVoteSystemQueueUrl())
                .messageBody(toJson(message))
                .build());
    }

    private String toJson(VoteMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize vote message", exception);
        }
    }
}
