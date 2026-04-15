package com.dbserver.voting_system.adapters.out.fake;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dbserver.voting_system.domain.exception.InvalidCpfException;
import org.junit.jupiter.api.Test;

class FakeCpfEligibilityClientAdapterTest {

    private final FakeCpfEligibilityClientAdapter adapter = new FakeCpfEligibilityClientAdapter();

    @Test
    void shouldAcceptCpfWithOnlyDigits_method_verify_do() {
        assertDoesNotThrow(() -> adapter.verify("12106842686"));
    }

    @Test
    void shouldAcceptCpfWithMask_method_verify_do() {
        assertDoesNotThrow(() -> adapter.verify("121.068.426-86"));
    }

    @Test
    void shouldRejectRepeatedDigitsCpf_method_verify_do() {
        assertThrows(InvalidCpfException.class, () -> adapter.verify("11111111111"));
    }
}
