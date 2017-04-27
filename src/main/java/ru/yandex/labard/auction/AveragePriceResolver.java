package ru.yandex.labard.auction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

class AveragePriceResolver implements PriceResolver {
    @Override
    public BigDecimal getOptimalPrice(Set<BigDecimal> prices) {
        return prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(prices.size()), 2, BigDecimal.ROUND_UP);
    }
}
