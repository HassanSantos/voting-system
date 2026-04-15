package com.dbserver.voting_system.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dbserver.voting_system.domain.enums.VoteValue;
import com.dbserver.voting_system.domain.model.Vote;
import com.dbserver.voting_system.domain.model.VotingResult;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class VotingResultCalculatorTest {

    private final VotingResultCalculator calculator = new VotingResultCalculator();

    @Test
    void shouldReturnApprovedOutcome_method_calculate_do() {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");

        VotingResult result = calculator.calculate("agenda-1", List.of(
                new Vote("agenda-1", "associate-1", VoteValue.YES, now),
                new Vote("agenda-1", "associate-2", VoteValue.YES, now),
                new Vote("agenda-1", "associate-3", VoteValue.NO, now)
        ));

        assertEquals(2, result.getYesVotes());
        assertEquals(1, result.getNoVotes());
        assertEquals(3, result.getTotalVotes());
        assertEquals("APPROVED", result.getOutcome());
    }

    @Test
    void shouldReturnRejectedOutcome_method_calculate_do() {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");

        VotingResult result = calculator.calculate("agenda-1", List.of(
                new Vote("agenda-1", "associate-1", VoteValue.NO, now),
                new Vote("agenda-1", "associate-2", VoteValue.NO, now),
                new Vote("agenda-1", "associate-3", VoteValue.YES, now)
        ));

        assertEquals("REJECTED", result.getOutcome());
    }

    @Test
    void shouldReturnTiedOutcome_method_calculate_do() {
        Instant now = Instant.parse("2026-01-01T10:00:00Z");

        VotingResult result = calculator.calculate("agenda-1", List.of(
                new Vote("agenda-1", "associate-1", VoteValue.NO, now),
                new Vote("agenda-1", "associate-2", VoteValue.YES, now)
        ));

        assertEquals(1, result.getYesVotes());
        assertEquals(1, result.getNoVotes());
        assertEquals(2, result.getTotalVotes());
        assertEquals("TIED", result.getOutcome());
    }
}
