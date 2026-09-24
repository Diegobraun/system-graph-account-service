package com.example.account.messaging;

import java.math.BigDecimal;

public final class DebitEvents {

    public record PaymentCompleted(String paymentId, Long accountId, BigDecimal amount) {
    }

    public record InvestmentApplied(String investmentId, Long accountId, BigDecimal amount, String productCode) {
    }

    private DebitEvents() {
    }
}
