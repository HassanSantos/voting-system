package com.dbserver.voting_system.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dbserver.voting_system.application.dto.response.AgendaResponse;
import com.dbserver.voting_system.application.port.in.CreateAgendaUseCase;
import com.dbserver.voting_system.config.GlobalExceptionHandler;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AgendaControllerTest {

    @Mock
    private CreateAgendaUseCase createAgendaUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setup_method_do() {
        AgendaController controller = new AgendaController(createAgendaUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateAgenda_method_createAgenda_do() throws Exception {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");

        when(createAgendaUseCase.execute(any()))
                .thenReturn(new AgendaResponse("agenda-1", "Pauta", "Descricao", now));

        mockMvc.perform(post("/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Pauta",
                                  "description": "Descricao"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("agenda-1"))
                .andExpect(jsonPath("$.title").value("Pauta"));

        verify(createAgendaUseCase).execute(any());
    }

    @Test
    void shouldReturnBadRequest_method_createAgenda_do() throws Exception {
        when(createAgendaUseCase.execute(any())).thenThrow(new IllegalArgumentException("title is required"));

        mockMvc.perform(post("/agendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "description": "Descricao"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("title is required"));
    }
}
