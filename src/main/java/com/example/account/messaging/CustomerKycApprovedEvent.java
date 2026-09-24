package com.example.account.messaging;

public record CustomerKycApprovedEvent(Long customerId, Long accountId, String riskTier) {
}
