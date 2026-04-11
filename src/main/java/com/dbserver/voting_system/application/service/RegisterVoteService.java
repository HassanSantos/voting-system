package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.request.RegisterVoteCommand;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.in.RegisterVoteUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.DuplicateVoteException;
import com.dbserver.voting_system.domain.exception.VotingSessionClosedException;
import com.dbserver.voting_system.domain.exception.VotingSessionNotFoundException;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterVoteService implements RegisterVoteUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final VotingSessionRepositoryPort votingSessionRepositoryPort;
    private final VoteRepositoryPort voteRepositoryPort;
    private final Clock clock;

    @Override
    public VoteResponse execute(RegisterVoteCommand command) {
        agendaRepositoryPort.findById(command.agendaId())
                .orElseThrow(() -> new AgendaNotFoundException(command.agendaId()));

        VotingSession session = votingSessionRepositoryPort.findByAgendaId(command.agendaId())
                .orElseThrow(() -> new VotingSessionNotFoundException(command.agendaId()));

        if (!session.isOpen(Instant.now(clock))) {
            throw new VotingSessionClosedException(command.agendaId());
        }

        boolean alreadyVoted = voteRepositoryPort
                .existsByAgendaIdAndAssociateId(command.agendaId(), command.associateId());

        if (alreadyVoted) {
            throw new DuplicateVoteException(command.agendaId(), command.associateId());
        }

        Vote vote = new Vote(
                command.agendaId(),
                command.associateId(),
                command.voteValue(),
                Instant.now(clock)
        );

        voteRepositoryPort.save(vote);

        return new VoteResponse(
                vote.getAgendaId(),
                vote.getAssociateId(),
                vote.getValue().name(),
                vote.getVotedAt()
        );
    }
}
