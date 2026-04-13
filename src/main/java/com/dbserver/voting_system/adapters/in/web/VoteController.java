package com.dbserver.voting_system.adapters.in.web;

import com.dbserver.voting_system.adapters.in.web.dto.RegisterVoteRequest;
import com.dbserver.voting_system.application.dto.request.RegisterVoteCommand;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.in.GetAllVotesUseCase;
import com.dbserver.voting_system.application.port.in.GetVotesByAgendaUseCase;
import com.dbserver.voting_system.application.port.in.RegisterVoteUseCase;
import com.dbserver.voting_system.domain.enums.VoteValue;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendas")
@RequiredArgsConstructor
public class VoteController {

    private final RegisterVoteUseCase registerVoteUseCase;
    private final GetAllVotesUseCase getAllVotesUseCase;
    private final GetVotesByAgendaUseCase getVotesByAgendaUseCase;

    @PostMapping("/{agendaId}/votes")
    @ResponseStatus(HttpStatus.CREATED)
    public VoteResponse vote(@PathVariable String agendaId, @RequestBody RegisterVoteRequest request) {
        RegisterVoteCommand command = new RegisterVoteCommand(
                agendaId,
                request.associateId(),
                VoteValue.valueOf(request.vote().toUpperCase())
        );

        return registerVoteUseCase.execute(command);
    }

    @GetMapping("/{agendaId}/votes")
    public List<VoteResponse> listVotes(@PathVariable String agendaId) {
        return getVotesByAgendaUseCase.execute(agendaId);
    }

    @GetMapping("/votes")
    public List<VoteResponse> listAllVotes() {
        return getAllVotesUseCase.execute();
    }
}
