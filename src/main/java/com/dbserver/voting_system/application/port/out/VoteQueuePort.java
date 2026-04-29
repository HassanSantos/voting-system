package com.dbserver.voting_system.application.port.out;

import com.dbserver.voting_system.domain.model.Vote;

public interface VoteQueuePort {

    void publish(Vote vote);
}
