package com.dbserver.voting_system.adapters.in.web;

import com.dbserver.voting_system.adapters.in.web.dto.OpenVotingSessionRequest;
import com.dbserver.voting_system.adapters.in.web.generated.api.VotingSessionApi;
import com.dbserver.voting_system.application.dto.request.OpenVotingSessionCommand;
import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;
import com.dbserver.voting_system.application.port.in.OpenVotingSessionUseCase;
import com.dbserver.voting_system.common.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.Routes.AGENDAS_BASE_PATH)
@RequiredArgsConstructor
public class VotingSessionController implements VotingSessionApi {

    private final OpenVotingSessionUseCase openVotingSessionUseCase;

    @Override
    @PostMapping(AppConstants.Routes.AGENDA_SESSIONS_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public VotingSessionResponse openVotingSession(String agendaId, OpenVotingSessionRequest request) {
        Long durationMinutes = request == null ? null : request.durationMinutes();
        OpenVotingSessionCommand command = new OpenVotingSessionCommand(agendaId, durationMinutes);
        return openVotingSessionUseCase.execute(command);
    }
}
