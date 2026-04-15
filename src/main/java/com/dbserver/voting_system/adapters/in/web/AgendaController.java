package com.dbserver.voting_system.adapters.in.web;

import com.dbserver.voting_system.adapters.in.web.dto.CreateAgendaRequest;
import com.dbserver.voting_system.application.dto.request.CreateAgendaCommand;
import com.dbserver.voting_system.application.dto.response.AgendaResponse;
import com.dbserver.voting_system.application.port.in.CreateAgendaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendas")
public class AgendaController {

    private final CreateAgendaUseCase createAgendaUseCase;

    public AgendaController(CreateAgendaUseCase createAgendaUseCase) {
        this.createAgendaUseCase = createAgendaUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendaResponse createAgenda(@RequestBody CreateAgendaRequest request) {
        return createAgendaUseCase.execute(new CreateAgendaCommand(request.title(), request.description()));
    }
}
