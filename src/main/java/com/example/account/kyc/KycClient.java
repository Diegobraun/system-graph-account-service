package com.example.account.kyc;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${services.customer-service.url}", path = "/kyc")
public interface KycClient {

    @GetMapping("/{customerId}")
    KycSummary status(@PathVariable("customerId") Long customerId);

    @GetMapping("/{customerId}/documents")
    List<KycDocument> documents(@PathVariable("customerId") Long customerId);
}
