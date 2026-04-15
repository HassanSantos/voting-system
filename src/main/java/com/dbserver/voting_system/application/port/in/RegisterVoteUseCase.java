package com.dbserver.voting_system.application.port.in;

import com.dbserver.voting_system.application.dto.request.RegisterVoteCommand;
import com.dbserver.voting_system.application.dto.response.VoteResponse;

public interface RegisterVoteUseCase {

    VoteResponse execute(RegisterVoteCommand command);
}
