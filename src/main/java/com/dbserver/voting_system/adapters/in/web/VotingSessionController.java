package com.dbserver.voting_system.adapters.in.web;

import com.dbserver.voting_system.adapters.in.web.dto.OpenVotingSessionRequest;
import com.dbserver.voting_system.adapters.in.web.generated.api.VotingSessionApi;
import com.dbserver.voting_system.adapters.in.web.mapper.WebCommandMapper;
import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;
import com.dbserver.voting_system.application.port.in.OpenVotingSessionUseCase;
import com.dbserver.voting_system.common.AppConstants;
import jakarta.validation.Valid;
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
    private final WebCommandMapper webCommandMapper;

    @Override
    @PostMapping(AppConstants.Routes.AGENDA_SESSIONS_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public VotingSessionResponse openVotingSession(String agendaId, @Valid OpenVotingSessionRequest request) {
        return openVotingSessionUseCase.execute(webCommandMapper.toCommand(agendaId, request));
    }
}
