package com.dbserver.voting_system.application.service;

import com.dbserver.voting_system.application.dto.request.RegisterVoteCommand;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.port.in.RegisterVoteUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.CpfEligibilityPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.domain.enums.CpfEligibilityStatus;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.UnableToVoteException;
import com.dbserver.voting_system.domain.exception.VotingSessionClosedException;
import com.dbserver.voting_system.domain.exception.VotingSessionNotFoundException;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingSession;
import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterVoteService implements RegisterVoteUseCase {

    private final AgendaRepositoryPort agendaRepositoryPort;
    private final VotingSessionRepositoryPort votingSessionRepositoryPort;
    private final VoteRepositoryPort voteRepositoryPort;
    private final CpfEligibilityPort cpfEligibilityPort;
    private final Clock clock;
    private final ApplicationResponseMapper responseMapper;

    @Override
    public VoteResponse execute(RegisterVoteCommand command) {
        agendaRepositoryPort.findById(command.agendaId())
                .orElseThrow(() -> new AgendaNotFoundException(command.agendaId()));

        VotingSession session = votingSessionRepositoryPort.findByAgendaId(command.agendaId())
                .orElseThrow(() -> new VotingSessionNotFoundException(command.agendaId()));

        if (!session.isOpen(Instant.now(clock))) {
            throw new VotingSessionClosedException(command.agendaId());
        }

        CpfEligibilityStatus cpfEligibilityStatus = cpfEligibilityPort.verify(command.cpf());
        if (cpfEligibilityStatus == CpfEligibilityStatus.UNABLE_TO_VOTE) {
            throw new UnableToVoteException(command.cpf());
        }

        Vote vote = new Vote(
                command.agendaId(),
                command.cpf(),
                command.voteValue(),
                Instant.now(clock)
        );

        return responseMapper.toVoteResponse(voteRepositoryPort.saveIfAbsent(vote));
    }
}
