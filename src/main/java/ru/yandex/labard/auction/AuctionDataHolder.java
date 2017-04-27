package ru.yandex.labard.auction;

import java.io.IOException;
import java.util.List;

interface AuctionDataHolder<E> {

    void init() throws IOException;

    List<E> getSellData();

    List<E> getBuyData();

}
