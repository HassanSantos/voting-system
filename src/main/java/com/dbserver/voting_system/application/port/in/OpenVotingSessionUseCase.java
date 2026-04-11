package com.dbserver.voting_system.application.port.in;

import com.dbserver.voting_system.application.dto.request.OpenVotingSessionCommand;
import com.dbserver.voting_system.application.dto.response.VotingSessionResponse;

public interface OpenVotingSessionUseCase {

    VotingSessionResponse execute(OpenVotingSessionCommand command);
}
