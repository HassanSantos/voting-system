package com.dbserver.voting_system.adapters.in.web;

import com.dbserver.voting_system.adapters.in.web.dto.OpenVotingSessionRequest;
import com.dbserver.voting_system.application.dto.request.OpenVotingSessionCommand;
import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;
import com.dbserver.voting_system.application.port.in.OpenVotingSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendas")
@RequiredArgsConstructor
public class VotingSessionController {

    private final OpenVotingSessionUseCase openVotingSessionUseCase;

    @PostMapping("/{agendaId}/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public VotingSessionResponse openSession(
            @PathVariable String agendaId,
            @RequestBody(required = false) OpenVotingSessionRequest request
    ) {
        Long durationMinutes = request == null ? null : request.durationMinutes();
        OpenVotingSessionCommand command = new OpenVotingSessionCommand(agendaId, durationMinutes);
        return openVotingSessionUseCase.execute(command);
    }
}
