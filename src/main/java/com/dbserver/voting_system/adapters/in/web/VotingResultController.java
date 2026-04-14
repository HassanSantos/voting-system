package com.dbserver.voting_system.adapters.in.web;

import com.dbserver.voting_system.application.dto.response.VotingResultResponse;
import com.dbserver.voting_system.application.port.in.GetVotingResultUseCase;
import com.dbserver.voting_system.common.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.Routes.AGENDAS_BASE_PATH)
@RequiredArgsConstructor
public class VotingResultController {

    private final GetVotingResultUseCase getVotingResultUseCase;

    @GetMapping(AppConstants.Routes.AGENDA_RESULT_PATH)
    public VotingResultResponse getResult(@PathVariable String agendaId) {
        return getVotingResultUseCase.execute(agendaId);
    }
}
