package ru.yandex.labard.auction;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;

public class SimpleTests {
    @Test
    public void amountForSaleTest() {
        final Request buyRequest = new Request(100, new BigDecimal("100.20"), RequestType.BUY);
        List<Request> sellRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("100.20"), RequestType.SELL));
            add(new Request(20, new BigDecimal("100"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200.20"), RequestType.SELL));
        }};
        Collections.sort(sellRequests);
        Assert.assertTrue(Auction.getAmountForSale(sellRequests, buyRequest) == 220);
    }

    @Test
    public void amountForSaleTestDuplicates() {
        final Request buyRequest = new Request(100, new BigDecimal("100.20"), RequestType.BUY);
        List<Request> sellRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200.20"), RequestType.SELL));
            add(new Request(100, new BigDecimal("100.20"), RequestType.SELL));
            add(new Request(100, new BigDecimal("100.20"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200.20"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("100.20"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200.20"), RequestType.SELL));
            add(new Request(20, new BigDecimal("100"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200.20"), RequestType.SELL));
        }};
        Collections.sort(sellRequests);
        Assert.assertTrue(Auction.getAmountForSale(sellRequests, buyRequest) == 720);
    }

    @Test
    public void amountForSaleTestEmpty() {
        final Request buyRequest = new Request(100, new BigDecimal("20.20"), RequestType.BUY);
        List<Request> sellRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("100.20"), RequestType.SELL));
            add(new Request(100, new BigDecimal("100"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200.20"), RequestType.SELL));
        }};
        Collections.sort(sellRequests);
        Assert.assertTrue(Auction.getAmountForSale(sellRequests, buyRequest) == 0);
    }

    @Test
    public void maxDealAmount() {
        List<Request> buyRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("100"), RequestType.BUY));
            add(new Request(100, new BigDecimal("150"), RequestType.BUY));
            add(new Request(100, new BigDecimal("50"), RequestType.BUY));
            add(new Request(100, new BigDecimal("200"), RequestType.BUY));
        }};
        List<Request> sellRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("100"), RequestType.SELL));
            add(new Request(100, new BigDecimal("150"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200"), RequestType.SELL));
        }};
        Collections.sort(sellRequests);
        Collections.sort(buyRequests, (first, second) -> second.compareTo(first));

        final Set<BigDecimal> biggestAmountPrices = new HashSet<>();
        Assert.assertThat(Auction.getMaxDealAmount(sellRequests, buyRequests, biggestAmountPrices), is(200));
        final Set<BigDecimal> goodSet = new HashSet<>();
        goodSet.add(new BigDecimal("200"));
        goodSet.add(new BigDecimal("150"));
        goodSet.add(new BigDecimal("100"));
        Assert.assertThat(biggestAmountPrices, is(goodSet));
    }

    @Test
    public void maxDealAmountPart() {
        List<Request> buyRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("150"), RequestType.BUY));
            add(new Request(1, new BigDecimal("100"), RequestType.BUY));
        }};
        List<Request> sellRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("100"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50"), RequestType.SELL));
        }};
        Collections.sort(sellRequests);
        Collections.sort(buyRequests, (first, second) -> second.compareTo(first));

        final Set<BigDecimal> biggestAmountPrices = new HashSet<>();
        Assert.assertThat(Auction.getMaxDealAmount(sellRequests, buyRequests, biggestAmountPrices), is(101));
        final Set<BigDecimal> goodSet = new HashSet<>();
        goodSet.add(new BigDecimal("150"));
        goodSet.add(new BigDecimal("100"));
        Assert.assertThat(biggestAmountPrices, is(goodSet));
    }

    @Test
    public void maxDealAmountSeveralPrices() {
        List<Request> buyRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("100.20"), RequestType.BUY));
            add(new Request(100, new BigDecimal("100"), RequestType.BUY));
            add(new Request(100, new BigDecimal("50.50"), RequestType.BUY));
            add(new Request(100, new BigDecimal("200"), RequestType.BUY));
            add(new Request(100, new BigDecimal("300"), RequestType.BUY));
        }};
        List<Request> sellRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("100.20"), RequestType.SELL));
            add(new Request(100, new BigDecimal("100"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200"), RequestType.SELL));
        }};
        Collections.sort(sellRequests);
        Collections.sort(buyRequests, (first, second) -> second.compareTo(first));

        final Set<BigDecimal> biggestAmountPrices = new HashSet<>();
        Assert.assertThat(Auction.getMaxDealAmount(sellRequests, buyRequests, biggestAmountPrices), is(300));
        final Set<BigDecimal> goodSet = new HashSet<>();
        goodSet.add(new BigDecimal("300"));
        goodSet.add(new BigDecimal("200"));
        goodSet.add(new BigDecimal("100.20"));
        Assert.assertThat(biggestAmountPrices, is(goodSet));
    }

    @Test
    public void maxDealAmountWithDuplicates() {
        List<Request> buyRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("100.20"), RequestType.BUY));
            add(new Request(100, new BigDecimal("100"), RequestType.BUY));
            add(new Request(100, new BigDecimal("50.50"), RequestType.BUY));
            add(new Request(100, new BigDecimal("200"), RequestType.BUY));
            add(new Request(100, new BigDecimal("300"), RequestType.BUY));
            add(new Request(100, new BigDecimal("200"), RequestType.BUY));
            add(new Request(100, new BigDecimal("300"), RequestType.BUY));
        }};
        List<Request> sellRequests = new ArrayList<Request>() {{
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200"), RequestType.SELL));
            add(new Request(100, new BigDecimal("100.20"), RequestType.SELL));
            add(new Request(100, new BigDecimal("100"), RequestType.SELL));
            add(new Request(100, new BigDecimal("50.50"), RequestType.SELL));
            add(new Request(100, new BigDecimal("200"), RequestType.SELL));
        }};
        Collections.sort(sellRequests);
        Collections.sort(buyRequests, (first, second) -> second.compareTo(first));

        final Set<BigDecimal> biggestAmountPrices = new HashSet<>();
        Assert.assertTrue(Auction.getMaxDealAmount(sellRequests, buyRequests, biggestAmountPrices) == 400);
        final Set<BigDecimal> goodSet = new HashSet<>();
        goodSet.add(new BigDecimal("300"));
        goodSet.add(new BigDecimal("200"));
        goodSet.add(new BigDecimal("100.20"));
        Assert.assertThat(biggestAmountPrices, is(goodSet));
    }

    @Test
    public void optimalPriceTest() {
        final AveragePriceResolver averagePriceResolver = new AveragePriceResolver();
        final Set<BigDecimal> set = new HashSet<>();
        set.add(new BigDecimal("300"));
        set.add(new BigDecimal("200"));
        set.add(new BigDecimal("100.20"));
        Assert.assertThat(averagePriceResolver.getOptimalPrice(set), is(new BigDecimal("200.07")));
    }
}
