package ru.yandex.labard.auction;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;


class FileDataHolder implements AuctionDataHolder<Request> {
    private final List<Request> saleList = new ArrayList<>();
    private final List<Request> buyList = new ArrayList<>();

    private final String sourceFile;
    private final SerializationLogic logic;

    FileDataHolder(String sourceFile, SerializationLogic logic) {
        this.sourceFile = sourceFile;
        this.logic = logic;
    }

    @Override
    public void init() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(sourceFile))) {
            stream.forEach(line -> {
                final Request request = logic.parse(line);
                if (request != null) {
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
                }
            });
        }
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
