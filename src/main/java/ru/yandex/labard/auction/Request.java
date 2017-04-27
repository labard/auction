package ru.yandex.labard.auction;

import java.math.BigDecimal;

class Request implements Comparable<Request> {
    private final int amount;
    private final BigDecimal price;
    private final RequestType type;

    Request(int amount, BigDecimal price, RequestType type) {
        this.amount = amount;
        this.price = price;
        this.type = type;
    }

    @Override
    public int compareTo(Request that) {
        return this.price.compareTo(that.price);
    }

    int getAmount() {
        return amount;
    }

    BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Request{" +
                "amount=" + amount +
                ", price=" + price +
                ", type=" + type +
                '}';
    }

    public RequestType getType() {
        return type;
    }
}
