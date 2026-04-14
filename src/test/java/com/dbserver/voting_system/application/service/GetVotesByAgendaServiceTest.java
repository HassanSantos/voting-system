package com.dbserver.voting_system.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.model.Agenda;
import com.dbserver.voting_system.domain.model.Vote;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetVotesByAgendaServiceTest {

    @Mock
    private AgendaRepositoryPort agendaRepositoryPort;

    @Mock
    private VoteRepositoryPort voteRepositoryPort;

    private GetVotesByAgendaService service;

    @BeforeEach
    void setup_method_do() {
        service = new GetVotesByAgendaService(
                agendaRepositoryPort,
                voteRepositoryPort,
                new ApplicationResponseMapper()
        );
    }

    @Test
    void shouldReturnVotesByAgenda_method_execute_do() {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");
        String agendaId = "agenda-1";

        when(agendaRepositoryPort.findById(agendaId))
                .thenReturn(Optional.of(new Agenda(agendaId, "Pauta", "Descricao", now.minusSeconds(60))));
        when(voteRepositoryPort.findByAgendaId(agendaId)).thenReturn(List.of(
                new Vote(agendaId, "associate-1", VoteValue.YES, now),
                new Vote(agendaId, "associate-2", VoteValue.NO, now)
        ));

        List<VoteResponse> response = service.execute(agendaId);

        assertEquals(2, response.size());
        assertEquals("YES", response.getFirst().voteValue());
        assertEquals("NO", response.get(1).voteValue());
    }

    @Test
    void shouldThrowAgendaNotFoundException_method_execute_do() {
        when(agendaRepositoryPort.findById("agenda-1")).thenReturn(Optional.empty());

        assertThrows(AgendaNotFoundException.class, () -> service.execute("agenda-1"));

        verify(voteRepositoryPort, never()).findByAgendaId(any());
    }
}
