package com.rest.marketplace.infrastructure.gateways.clients.exchangerate;

import com.rest.marketplace.infrastructure.gateways.clients.exchangerate.model.ExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchange-rate-client", url = "${exchange-rate.url}")
public interface ExchangeRateClient {

	@GetMapping("/v6/{apiKey}/latest/ARS")
	ExchangeRateResponse getLatestRates(@PathVariable("apiKey") String apiKey);
}
