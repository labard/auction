package ru.yandex.labard.auction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

interface PriceResolver {
    BigDecimal getOptimalPrice(Set<BigDecimal> prices);
}
