package com.dbserver.voting_system.application.usecase;

public interface CreateVotingSessionUseCase {

    void execute(Long time, Long idAgenda);
}
