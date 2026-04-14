package com.dbserver.voting_system.adapters.in.web;

import com.dbserver.voting_system.adapters.in.web.dto.CreateAgendaRequest;
import com.dbserver.voting_system.adapters.in.web.generated.api.AgendaApi;
import com.dbserver.voting_system.adapters.in.web.mapper.WebCommandMapper;
import com.dbserver.voting_system.application.dto.response.AgendaResponse;
import com.dbserver.voting_system.application.port.in.CreateAgendaUseCase;
import com.dbserver.voting_system.common.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstants.Routes.AGENDAS_BASE_PATH)
@RequiredArgsConstructor
public class AgendaController implements AgendaApi {

    private final CreateAgendaUseCase createAgendaUseCase;
    private final WebCommandMapper webCommandMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public AgendaResponse createAgenda(@Valid @RequestBody CreateAgendaRequest request) {
        return createAgendaUseCase.execute(webCommandMapper.toCommand(request));
    }
}
