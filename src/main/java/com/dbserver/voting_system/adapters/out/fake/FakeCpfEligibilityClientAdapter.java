package com.dbserver.voting_system.adapters.out.fake;

import com.dbserver.voting_system.application.port.out.CpfEligibilityPort;
import com.dbserver.voting_system.domain.enums.CpfEligibilityStatus;
import com.dbserver.voting_system.domain.exception.InvalidCpfException;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class FakeCpfEligibilityClientAdapter implements CpfEligibilityPort {

    @Override
    public CpfEligibilityStatus verify(String cpf) {
        if (!isValidCpf(cpf)) {
            throw new InvalidCpfException(cpf);
        }

        return ThreadLocalRandom.current().nextBoolean()
                ? CpfEligibilityStatus.ABLE_TO_VOTE
                : CpfEligibilityStatus.UNABLE_TO_VOTE;
    }

    private boolean isValidCpf(String cpf) {
        if (cpf == null) {
            return false;
        }

        String sanitizedCpf = cpf.replaceAll("\\D", "");
        if (sanitizedCpf.length() != 11 || sanitizedCpf.chars().distinct().count() == 1) {
            return false;
        }

        return calculateDigit(sanitizedCpf, 9) == Character.getNumericValue(sanitizedCpf.charAt(9))
                && calculateDigit(sanitizedCpf, 10) == Character.getNumericValue(sanitizedCpf.charAt(10));
    }

    private int calculateDigit(String cpf, int length) {
        int sum = 0;
        int weight = length + 1;

        for (int index = 0; index < length; index++) {
            sum += Character.getNumericValue(cpf.charAt(index)) * (weight - index);
        }

        int remainder = (sum * 10) % 11;
        return remainder == 10 ? 0 : remainder;
    }
}
