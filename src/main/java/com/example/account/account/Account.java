package com.example.account.account;

import java.math.BigDecimal;
import java.time.Instant;

public record Account(Long id, Long customerId, BigDecimal balance, AccountStatus status, Instant openedAt) {

    public Account activate() {
        return new Account(id, customerId, balance, AccountStatus.ACTIVE, openedAt);
    }

    public Account debit(BigDecimal amount) {
        return new Account(id, customerId, balance.subtract(amount), status, openedAt);
    }

    public Account credit(BigDecimal amount) {
        return new Account(id, customerId, balance.add(amount), status, openedAt);
    }
}
