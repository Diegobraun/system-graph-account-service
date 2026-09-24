package com.example.account.messaging;

import com.example.account.account.AccountService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerKycApprovedListener {

    private final AccountService service;

    public CustomerKycApprovedListener(AccountService service) {
        this.service = service;
    }

    @KafkaListener(topics = "${app.topics.customer-kyc-approved}")
    public void onKycApproved(CustomerKycApprovedEvent event) {
        service.activate(event.accountId());
    }
}
