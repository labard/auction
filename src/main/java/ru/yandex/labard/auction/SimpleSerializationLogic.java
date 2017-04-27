package ru.yandex.labard.auction;

import java.math.BigDecimal;

class SimpleSerializationLogic implements SerializationLogic {
    @Override
    public Request parse(String string) {
        final String[] data = string.split(" ");
        if (data.length < 3) throw new IllegalArgumentException("Not enough data for parsing " + string);
        final int amount = Integer.parseInt(data[1]);
        final BigDecimal price = new BigDecimal(data[2]);
        switch (data[0]) {
            case "S": {
                return new Request(amount, price, RequestType.SELL);
            }
            case "B": {
                return new Request(amount, price, RequestType.BUY);
            }
            default:
                throw new IllegalArgumentException("Unsupported request type " + data[0]);
        }
    }
}
