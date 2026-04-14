package com.dbserver.voting_system.config;

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
}
