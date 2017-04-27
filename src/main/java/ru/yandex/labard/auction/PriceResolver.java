package ru.yandex.labard.auction;

import java.math.BigDecimal;
import java.util.List;

interface PriceResolver {
    BigDecimal getOptimalPrice(List<BigDecimal> prices);
}
