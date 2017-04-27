package ru.yandex.labard.auction;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

public class Auction {
    private final PriceResolver resolver = new AveragePriceResolver();
    private static final String BAD_AUCTION = "0 n/a";

    public static void main(String[] args) {
        final Auction auction = new Auction();
        final AuctionDataHolder<Request> dataHolder = new FileDataHolder(args[0], new SimpleSerializationLogic());
        try {
            dataHolder.init();
        } catch (IOException e) {
            System.err.print("Error while getting data from resource: " + args[0] + ";\n" + e.getMessage());
            return;
        }
        final Request deals = auction.makeAuction(dataHolder.getSellData(), dataHolder.getBuyData());

        if (deals != null) {
            System.out.println(deals.getAmount() + " " + deals.getPrice());
        } else {
            System.out.print(BAD_AUCTION);
        }
    }

    public Request makeAuction(List<Request> saleList, List<Request> buyList) {
        if (saleList.isEmpty() || buyList.isEmpty()) return null;

        final Set<BigDecimal> biggestAmountPrices = new HashSet<>();

        Collections.sort(saleList);
        Collections.sort(buyList, (first, second) -> second.compareTo(first));

        final int maxDealAmount = getMaxDealAmount(saleList, buyList, biggestAmountPrices);

        if (maxDealAmount == 0) return null;

        return new Request(maxDealAmount, resolver.getOptimalPrice(biggestAmountPrices), RequestType.RESULT);
    }

    // только для сортированных листов
    public static int getMaxDealAmount(List<Request> saleList, List<Request> buyList, Set<BigDecimal> biggestAmountPrices) {
        int maxDealAmount = 0;
        int buyForHigherPrice = 0;
        for (Request buyRequest : buyList) {
            final int amountForSale = getAmountForSale(saleList, buyRequest);
            final int amountForBuy = buyRequest.getAmount() + buyForHigherPrice;
            final int dealAmount = Math.min(amountForBuy, amountForSale);
            if (dealAmount >= maxDealAmount) {
                maxDealAmount = dealAmount;
                biggestAmountPrices.add(buyRequest.getPrice());
            }
            buyForHigherPrice = amountForBuy;
        }
        return maxDealAmount;
    }

    //считаем возможное количество продаж по указанной цене
    public static int getAmountForSale(List<Request> saleList, Request buyRequest) {
        return saleList.stream()
                .filter(sellRequest -> buyRequest.compareTo(sellRequest) >= 0)
                .mapToInt(Request::getAmount).sum();
    }


}

