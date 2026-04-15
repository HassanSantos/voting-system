package com.dbserver.voting_system.adapters.in.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dbserver.voting_system.application.dto.response.VotingResultResponse;
import com.dbserver.voting_system.application.port.in.GetVotingResultUseCase;
import com.dbserver.voting_system.config.GlobalExceptionHandler;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class VotingResultControllerTest {

    @Mock
    private GetVotingResultUseCase getVotingResultUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setup_method_do() {
        VotingResultController controller = new VotingResultController(getVotingResultUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnVotingResult_method_getResult_do() throws Exception {
        when(getVotingResultUseCase.execute("agenda-1"))
                .thenReturn(new VotingResultResponse("agenda-1", 2, 1, 3, "APPROVED"));

        mockMvc.perform(get("/agendas/agenda-1/result"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendaId").value("agenda-1"))
                .andExpect(jsonPath("$.outcome").value("APPROVED"));
    }

    @Test
    void shouldReturnNotFound_method_getResult_do() throws Exception {
        when(getVotingResultUseCase.execute("agenda-1")).thenThrow(new AgendaNotFoundException("agenda-1"));

        mockMvc.perform(get("/agendas/agenda-1/result"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Agenda not found: agenda-1"));
    }
}
