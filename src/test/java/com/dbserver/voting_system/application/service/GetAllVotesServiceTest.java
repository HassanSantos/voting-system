package com.dbserver.voting_system.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.dbserver.voting_system.application.mapper.ApplicationResponseMapper;
import com.dbserver.voting_system.application.dto.response.VoteResponse;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.model.Vote;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllVotesServiceTest {

    @Mock
    private VoteRepositoryPort voteRepositoryPort;

    private GetAllVotesService service;

    @BeforeEach
    void setup_method_do() {
        service = new GetAllVotesService(voteRepositoryPort, new ApplicationResponseMapper());
    }

    @Test
    void shouldReturnAllVotes_method_execute_do() {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");
        when(voteRepositoryPort.findAll()).thenReturn(List.of(
                new Vote("agenda-1", "associate-1", VoteValue.YES, now),
                new Vote("agenda-2", "associate-2", VoteValue.NO, now)
        ));

        List<VoteResponse> response = service.execute();

        assertEquals(2, response.size());
        assertEquals("agenda-1", response.getFirst().agendaId());
        assertEquals("YES", response.getFirst().voteValue());
        assertEquals("NO", response.get(1).voteValue());
    }
}
