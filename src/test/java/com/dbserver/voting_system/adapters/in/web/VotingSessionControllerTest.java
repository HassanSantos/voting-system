package com.dbserver.voting_system.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;
import com.dbserver.voting_system.application.port.in.OpenVotingSessionUseCase;
import com.dbserver.voting_system.config.GlobalExceptionHandler;
import com.dbserver.voting_system.domain.exception.VotingSessionAlreadyOpenException;
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
class VotingSessionControllerTest {

    @Mock
    private OpenVotingSessionUseCase openVotingSessionUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setup_method_do() {
        VotingSessionController controller = new VotingSessionController(openVotingSessionUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldOpenVotingSession_method_openSession_do() throws Exception {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");

        when(openVotingSessionUseCase.execute(any()))
                .thenReturn(new VotingSessionResponse("agenda-1", now, now.plusSeconds(300), "OPEN"));

        mockMvc.perform(post("/agendas/agenda-1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "durationMinutes": 5
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.agendaId").value("agenda-1"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(openVotingSessionUseCase).execute(any());
    }

    @Test
    void shouldReturnConflictWhenSessionAlreadyOpen_method_openSession_do() throws Exception {
        when(openVotingSessionUseCase.execute(any())).thenThrow(new VotingSessionAlreadyOpenException("agenda-1"));

        mockMvc.perform(post("/agendas/agenda-1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Voting session is already open for agenda: agenda-1"));
    }
}
