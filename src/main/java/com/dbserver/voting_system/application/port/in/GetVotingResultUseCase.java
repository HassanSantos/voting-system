package com.dbserver.voting_system.application.port.in;

import com.dbserver.voting_system.application.dto.response.VotingResultResponse;

public interface GetVotingResultUseCase {

    VotingResultResponse execute(String agendaId);
}
