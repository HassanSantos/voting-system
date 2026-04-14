package com.dbserver.voting_system.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.in.GetAllVotesUseCase;
import com.dbserver.voting_system.application.port.in.GetVotesByAgendaUseCase;
import com.dbserver.voting_system.application.port.in.RegisterVoteUseCase;
import com.dbserver.voting_system.config.GlobalExceptionHandler;
import com.dbserver.voting_system.domain.exception.DuplicateVoteException;
import com.dbserver.voting_system.domain.exception.InvalidCpfException;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class VoteControllerTest {

    @Mock
    private RegisterVoteUseCase registerVoteUseCase;

    @Mock
    private GetAllVotesUseCase getAllVotesUseCase;

    @Mock
    private GetVotesByAgendaUseCase getVotesByAgendaUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setup_method_do() {
        VoteController controller = new VoteController(registerVoteUseCase, getAllVotesUseCase, getVotesByAgendaUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldRegisterVote_method_vote_do() throws Exception {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");

        when(registerVoteUseCase.execute(any()))
                .thenReturn(new VoteResponse("agenda-1", "12345678900", "YES", now));

        mockMvc.perform(post("/agendas/agenda-1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cpf": "12345678900",
                                  "vote": "yes"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.agendaId").value("agenda-1"))
                .andExpect(jsonPath("$.voteValue").value("YES"));

        verify(registerVoteUseCase).execute(any());
    }

    @Test
    void shouldReturnConflictWhenDuplicateVote_method_vote_do() throws Exception {
        when(registerVoteUseCase.execute(any())).thenThrow(new DuplicateVoteException("agenda-1", "12345678900"));

        mockMvc.perform(post("/agendas/agenda-1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cpf": "12345678900",
                                  "vote": "yes"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Vote already registered for CPF 12345678900 in agenda agenda-1"));
    }

    @Test
    void shouldReturnNotFoundWhenCpfIsInvalid_method_vote_do() throws Exception {
        when(registerVoteUseCase.execute(any())).thenThrow(new InvalidCpfException("12345678900"));

        mockMvc.perform(post("/agendas/agenda-1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cpf": "12345678900",
                                  "vote": "yes"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("CPF not found: 12345678900"));
    }

    @Test
    void shouldListVotesByAgenda_method_listVotes_do() throws Exception {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");
        when(getVotesByAgendaUseCase.execute("agenda-1"))
                .thenReturn(List.of(new VoteResponse("agenda-1", "12345678900", "YES", now)));

        mockMvc.perform(get("/agendas/agenda-1/votes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf").value("12345678900"));
    }

    @Test
    void shouldListAllVotes_method_listAllVotes_do() throws Exception {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");
        when(getAllVotesUseCase.execute())
                .thenReturn(List.of(new VoteResponse("agenda-1", "12345678900", "YES", now)));

        mockMvc.perform(get("/agendas/votes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].voteValue").value("YES"));
    }
}
