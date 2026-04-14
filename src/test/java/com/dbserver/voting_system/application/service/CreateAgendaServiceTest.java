package com.dbserver.voting_system.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.dto.request.CreateAgendaCommand;
import com.dbserver.voting_system.domain.exception.InvalidAgendaTitleException;
import com.dbserver.voting_system.application.dto.response.AgendaResponse;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.domain.model.Agenda;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateAgendaServiceTest {

    private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");

    @Mock
    private AgendaRepositoryPort agendaRepositoryPort;

    private CreateAgendaService service;

    @BeforeEach
    void setup_method_do() {
        service = new CreateAgendaService(
                agendaRepositoryPort,
                Clock.fixed(NOW, ZoneOffset.UTC),
                new ApplicationResponseMapper()
        );
    }

    @Test
    void shouldCreateAgenda_method_execute_do() {
        CreateAgendaCommand command = new CreateAgendaCommand("Pauta", "Descricao");

        when(agendaRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AgendaResponse response = service.execute(command);

        assertNotNull(response.id());
        assertEquals("Pauta", response.title());
        assertEquals("Descricao", response.description());
        assertEquals(NOW, response.createdAt());
        verify(agendaRepositoryPort).save(any(Agenda.class));
    }

    @Test
    void shouldThrowInvalidAgendaTitleException_method_execute_do() {
        CreateAgendaCommand command = new CreateAgendaCommand("   ", "Descricao");

        assertThrows(InvalidAgendaTitleException.class, () -> service.execute(command));

        verify(agendaRepositoryPort, never()).save(any());
    }
}
