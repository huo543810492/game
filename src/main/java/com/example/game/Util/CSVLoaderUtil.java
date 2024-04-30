package com.example.game.Util;

import com.example.game.dao.CSVImportStatusDao;
import com.example.game.model.CSVImportStatus;
import com.example.game.model.GameSales;
import com.example.game.service.GameSalesService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

@Slf4j
@Component
public class CSVLoaderUtil {

    private static final int BATCH_SIZE = 10000;

    @Autowired
    GameSalesService gameSalesServiceImpl;

    @Autowired
    CSVImportStatusDao csvImportStatusDao;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    public void loadCSVInBatch() {
        ThreadFactory factory = Thread.ofVirtual().name("my-virtual-thread-", 1).factory();
        ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);
        List<Future<?>> futures = new ArrayList<>();
        try (
            InputStream inputStream = CSVLoaderUtil.class.getClassLoader().getResourceAsStream("game_sales.csv");
            CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream)).build();
        ){
            if (inputStream == null) {
                throw new RuntimeException("Cannot find 'game_sales.csv'");
            }
            List<GameSales> salesRecords = new ArrayList<>();
            String[] nextRecord;
            Long startId = 0L;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                GameSales sales = mapToGameSales(nextRecord);
                salesRecords.add(sales);
                //track csv Import Status
                if(count==0){
                    startId = sales.getId();
                }
                count++;
                if (count >= BATCH_SIZE) {
                    submitBatch(executor, salesRecords, futures, startId, sales.getId());
                    salesRecords.clear();
                    count = 0;
                }
            }
            // CSV file read done
            futures.parallelStream().forEach(future -> {
                try {
                    future.get();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            });
            log.info("{} load csv file whole batch done", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("-----> catch loadCSVInBatch exception  - {}", e.toString());
        } finally {
            executor.shutdown();
            log.info("{} Executor shut down", Thread.currentThread().getName());
        }
    }


    private void submitBatch(ExecutorService executor, List<GameSales> salesRecords, List<Future<?>> futures, Long startId, Long endId) {
        List<GameSales> batch = new ArrayList<>(salesRecords);
        CSVImportStatus csvImportStatus = new CSVImportStatus();
        csvImportStatus.setStartId(startId);
        csvImportStatus.setEndId(endId);
        csvImportStatus.setCreateTime(new Date());
        csvImportStatusDao.insert(csvImportStatus);

        futures.add(executor.submit(() -> {
            log.info("{} start load csv file, one batch", Thread.currentThread().getName());
            gameSalesServiceImpl.saveBatch(batch);
            log.info("{} load csv file one batch done", Thread.currentThread().getName());
        }));
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
        sales.setDateOfSale(dateFormat.parse(fields[8].replace("\"", "")));
        sales.setCreateTime(dateFormat.parse(fields[9].replace("\"", "")));
        return sales;
    }
}
