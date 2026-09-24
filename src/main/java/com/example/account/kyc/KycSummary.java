package com.example.account.kyc;

public record KycSummary(Long customerId, String status, int score) {
}
