package com.example.account.kyc;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers/{id}")
public class CustomerKycController {

    private final KycClient kycClient;

    public CustomerKycController(KycClient kycClient) {
        this.kycClient = kycClient;
    }

    @GetMapping("/kyc")
    public KycSummary kyc(@PathVariable Long id) {
        return kycClient.status(id);
    }

    @GetMapping("/documents")
    public List<KycDocument> documents(@PathVariable Long id) {
        return kycClient.documents(id);
    }
}
