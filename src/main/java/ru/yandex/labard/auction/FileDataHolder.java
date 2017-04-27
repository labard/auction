package ru.yandex.labard.auction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class FileDataHolder implements AuctionDataHolder<Request> {
    private final List<Request> saleList = new ArrayList<>();
    private final List<Request> buyList = new ArrayList<>();

    private final File sourceFile;
    private final SerializationLogic logic;

    FileDataHolder(File sourceFile, SerializationLogic logic) {
        this.sourceFile = sourceFile;
        this.logic = logic;
    }

    @Override
    public void init() throws FileNotFoundException {
        Scanner in = new Scanner(sourceFile);
        in.forEachRemaining(s -> {
            final Request request = logic.parse(s);
            switch (request.getType()) {
                case SELL: {
                    saleList.add(request);
                    break;
                }
                case BUY: {
                    buyList.add(request);
                    break;
                }
                default:
                    break;
            }
        });
    }

    @Override
    public List<Request> getSellData() {
        return new ArrayList<>(saleList);
    }

    @Override
    public List<Request> getBuyData() {
        return new ArrayList<>(buyList);
    }
}
