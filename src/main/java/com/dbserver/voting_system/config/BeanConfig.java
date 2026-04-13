package com.dbserver.voting_system.config;

import com.dbserver.voting_system.application.port.in.CreateAgendaUseCase;
import com.dbserver.voting_system.application.port.in.GetAllVotesUseCase;
import com.dbserver.voting_system.application.port.in.GetVotesByAgendaUseCase;
import com.dbserver.voting_system.application.port.in.GetVotingResultUseCase;
import com.dbserver.voting_system.application.port.in.OpenVotingSessionUseCase;
import com.dbserver.voting_system.application.port.in.RegisterVoteUseCase;
import com.dbserver.voting_system.application.port.out.AgendaRepositoryPort;
import com.dbserver.voting_system.application.port.out.VoteRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingResultRepositoryPort;
import com.dbserver.voting_system.application.port.out.VotingSessionRepositoryPort;
import com.dbserver.voting_system.application.service.CreateAgendaService;
import com.dbserver.voting_system.application.service.GetAllVotesService;
import com.dbserver.voting_system.application.service.GetVotesByAgendaService;
import com.dbserver.voting_system.application.service.GetVotingResultService;
import com.dbserver.voting_system.application.service.OpenVotingSessionService;
import com.dbserver.voting_system.application.service.RegisterVoteService;
import com.dbserver.voting_system.domain.service.VotingResultCalculator;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    VotingResultCalculator votingResultCalculator() {
        return new VotingResultCalculator();
    }

    @Bean
    CreateAgendaUseCase createAgendaUseCase(AgendaRepositoryPort agendaRepositoryPort, Clock clock) {
        return new CreateAgendaService(agendaRepositoryPort, clock);
    }

    @Bean
    OpenVotingSessionUseCase openVotingSessionUseCase(
            AgendaRepositoryPort agendaRepositoryPort,
            VotingSessionRepositoryPort votingSessionRepositoryPort,
            Clock clock
    ) {
        return new OpenVotingSessionService(agendaRepositoryPort, votingSessionRepositoryPort, clock);
    }

    @Bean
    RegisterVoteUseCase registerVoteUseCase(
            AgendaRepositoryPort agendaRepositoryPort,
            VotingSessionRepositoryPort votingSessionRepositoryPort,
            VoteRepositoryPort voteRepositoryPort,
            Clock clock
    ) {
        return new RegisterVoteService(agendaRepositoryPort, votingSessionRepositoryPort, voteRepositoryPort, clock);
    }

    @Bean
    GetVotingResultUseCase getVotingResultUseCase(
            AgendaRepositoryPort agendaRepositoryPort,
            VoteRepositoryPort voteRepositoryPort,
            VotingResultRepositoryPort votingResultRepositoryPort,
            VotingResultCalculator votingResultCalculator
    ) {
        return new GetVotingResultService(
                agendaRepositoryPort,
                voteRepositoryPort,
                votingResultRepositoryPort,
                votingResultCalculator
        );
    }

    @Bean
    GetVotesByAgendaUseCase getVotesByAgendaUseCase(
            AgendaRepositoryPort agendaRepositoryPort,
            VoteRepositoryPort voteRepositoryPort
    ) {
        return new GetVotesByAgendaService(agendaRepositoryPort, voteRepositoryPort);
    }

    @Bean
    GetAllVotesUseCase getAllVotesUseCase(VoteRepositoryPort voteRepositoryPort) {
        return new GetAllVotesService(voteRepositoryPort);
    }
}
