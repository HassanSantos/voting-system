package com.dbserver.voting_system.application.port.in;

import com.dbserver.voting_system.application.dto.response.VoteResponse;
import java.util.List;

public interface GetAllVotesUseCase {

    List<VoteResponse> execute();
}
