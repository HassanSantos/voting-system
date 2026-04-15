package com.dbserver.voting_system.adapters.in.web.mapper;

import com.dbserver.voting_system.adapters.in.web.dto.CreateAgendaRequest;
import com.dbserver.voting_system.adapters.in.web.dto.OpenVotingSessionRequest;
import com.dbserver.voting_system.adapters.in.web.dto.RegisterVoteRequest;
import com.dbserver.voting_system.application.dto.request.CreateAgendaCommand;
import com.dbserver.voting_system.application.dto.request.OpenVotingSessionCommand;
import com.dbserver.voting_system.application.dto.request.RegisterVoteCommand;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.exception.InvalidVoteValueException;
import org.springframework.stereotype.Component;

@Component
public class WebCommandMapper {

    public CreateAgendaCommand toCommand(CreateAgendaRequest request) {
        return new CreateAgendaCommand(request.title(), request.description());
    }

    public OpenVotingSessionCommand toCommand(String agendaId, OpenVotingSessionRequest request) {
        Long durationMinutes = request == null ? null : request.durationMinutes();
        return new OpenVotingSessionCommand(agendaId, durationMinutes);
    }

    public RegisterVoteCommand toCommand(String agendaId, RegisterVoteRequest request) {
        return new RegisterVoteCommand(agendaId, request.cpf(), parseVoteValue(request.vote()));
    }

    private VoteValue parseVoteValue(String voteValue) {
        try {
            return VoteValue.valueOf(voteValue.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new InvalidVoteValueException(voteValue);
        }
    }
}
