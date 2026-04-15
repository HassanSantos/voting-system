package com.dbserver.voting_system.application.mapper;

import com.dbserver.voting_system.application.dto.response.AgendaResponse;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.dto.response.VotingResultResponse;
import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;
import com.dbserver.voting_system.domain.model.Agenda;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingResult;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class ApplicationResponseMapper {

    public AgendaResponse toAgendaResponse(Agenda agenda) {
        return new AgendaResponse(
                agenda.getId(),
                agenda.getTitle(),
                agenda.getDescription(),
                agenda.getCreatedAt()
        );
    }

    public VoteResponse toVoteResponse(Vote vote) {
        return new VoteResponse(
                vote.getAgendaId(),
                vote.getCpf(),
                vote.getValue().name(),
                vote.getVotedAt()
        );
    }

    public VotingSessionResponse toVotingSessionResponse(VotingSession session, Instant referenceTime) {
        return new VotingSessionResponse(
                session.getAgendaId(),
                session.getOpenedAt(),
                session.getEndsAt(),
                session.currentStatus(referenceTime).name()
        );
    }

    public VotingResultResponse toVotingResultResponse(VotingResult result) {
        return new VotingResultResponse(
                result.getAgendaId(),
                result.getYesVotes(),
                result.getNoVotes(),
                result.getTotalVotes(),
                result.getOutcome()
        );
    }
}
