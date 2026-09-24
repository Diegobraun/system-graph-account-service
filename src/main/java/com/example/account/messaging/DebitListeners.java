package com.example.account.messaging;

import com.example.account.account.AccountService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DebitListeners {

    private final AccountService service;

    public DebitListeners(AccountService service) {
        this.service = service;
    }

    @KafkaListener(topics = "${app.topics.payment-completed}")
    public void onPaymentCompleted(DebitEvents.PaymentCompleted event) {
        service.debit(event.accountId(), event.amount(), "payment:" + event.paymentId());
    }

    @KafkaListener(topics = "${app.topics.investment-applied}")
    public void onInvestmentApplied(DebitEvents.InvestmentApplied event) {
        service.debit(event.accountId(), event.amount(), "investment:" + event.investmentId());
    }
}
