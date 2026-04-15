package com.dbserver.voting_system.adapters.in.web;

import com.dbserver.voting_system.adapters.in.web.dto.RegisterVoteRequest;
import com.dbserver.voting_system.adapters.in.web.generated.api.VoteApi;
import com.dbserver.voting_system.adapters.in.web.mapper.WebCommandMapper;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.in.GetAllVotesUseCase;
import com.dbserver.voting_system.application.port.in.GetVotesByAgendaUseCase;
import com.dbserver.voting_system.application.port.in.RegisterVoteUseCase;
import com.dbserver.voting_system.common.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstants.Routes.AGENDAS_BASE_PATH)
@RequiredArgsConstructor
public class VoteController implements VoteApi {

    private final RegisterVoteUseCase registerVoteUseCase;
    private final GetAllVotesUseCase getAllVotesUseCase;
    private final GetVotesByAgendaUseCase getVotesByAgendaUseCase;
    private final WebCommandMapper webCommandMapper;

    @PostMapping(AppConstants.Routes.AGENDA_VOTES_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public VoteResponse registerVote(@PathVariable String agendaId, @Valid @RequestBody RegisterVoteRequest request) {
        return registerVoteUseCase.execute(webCommandMapper.toCommand(agendaId, request));
    }

    @GetMapping(AppConstants.Routes.AGENDA_VOTES_PATH)
    @Override
    public List<VoteResponse> listVotesByAgenda(@PathVariable String agendaId) {
        return getVotesByAgendaUseCase.execute(agendaId);
    }

    @GetMapping(AppConstants.Routes.VOTES_PATH)
    @Override
    public List<VoteResponse> listAllVotes() {
        return getAllVotesUseCase.execute();
    }
}
