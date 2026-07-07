package com.rest.marketplace.domain.ports.exchangerate;

import java.math.BigDecimal;

public interface ExchangeRatePort {

	BigDecimal getUsdRate();
}
