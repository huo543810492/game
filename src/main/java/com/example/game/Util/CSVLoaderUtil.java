package com.example.game.Util;

import com.example.game.model.GameSales;
import com.example.game.service.GameSalesService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
public class CSVLoaderUtil {

    private static final int BATCH_SIZE = 10000;

    @Autowired
    GameSalesService GameSalesServiceImpl;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    public void loadCSVInBatch() {
        InputStream inputStream = CSVLoaderUtil.class.getClassLoader().getResourceAsStream("game_sales.csv");
        ExecutorService executor = Executors.newFixedThreadPool(100);

        int count = 0;
        try {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find 'game_sales.csv'");
            }

            List<GameSales> salesRecords = new ArrayList<>();
            List<Future<?>> futures = new ArrayList<>();

            try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream)).build()) {
                String[] nextRecord;
                log.info(Thread.currentThread().getName() + " start load csv file, one batch");
                while ((nextRecord = csvReader.readNext()) != null) {
                    GameSales sales = mapToGameSales(nextRecord);
                    salesRecords.add(sales);
                    count++;
                    if (count >= BATCH_SIZE) {
                        List<GameSales> batch = new ArrayList<>(salesRecords);
                        futures.add(executor.submit(() -> GameSalesServiceImpl.saveBatch(batch)));
                        salesRecords.clear();
                        count = 0;
                        log.info(Thread.currentThread().getName() + " load csv file one batch done");
                    }
                }

                futures.parallelStream().forEach(future -> {
                    try {
                        future.get();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
                log.info(Thread.currentThread().getName() + " load csv file whole batch done");
            }
        } catch (IOException | CsvValidationException | ParseException e) {
            log.warn("-----> catch loadCSVInBatch exception  - {}", e.toString());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                executor.shutdown();
                log.info(Thread.currentThread().getName() + " Executor shut down");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private GameSales mapToGameSales(String[] fields) throws ParseException {
        GameSales sales = new GameSales();
        sales.setId(Long.parseLong(fields[0].replace("\"", "")));
        sales.setGameNo(Integer.parseInt(fields[1].replace("\"", "")));
        sales.setGameName(fields[2].replace("\"", ""));
        sales.setGameCode(fields[3].replace("\"", ""));
        sales.setType(Integer.parseInt(fields[4].replace("\"", "")));
        sales.setCostPrice(new BigDecimal(fields[5].replace("\"", "")));
        sales.setTax(new BigDecimal(fields[6].replace("\"", "")));
        sales.setSalePrice(new BigDecimal(fields[7].replace("\"", "")));
        sales.setDate(dateFormat.parse(fields[8].replace("\"", "")));
        sales.setCreate_time(dateFormat.parse(fields[9].replace("\"", "")));
        return sales;
    }
}
