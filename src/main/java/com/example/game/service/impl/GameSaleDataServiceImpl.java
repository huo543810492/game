package com.example.game.service.impl;

import com.example.game.Util.CSVLoaderUtil;
import com.example.game.service.GameSalesDataService;
import com.example.game.task.GenerateDataTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class GameSaleDataServiceImpl implements GameSalesDataService {

    @Autowired
    GenerateDataTask generateDataTask;

    @Autowired
    CSVLoaderUtil csvLoaderUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateData() {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            List<Future<?>> futures = new ArrayList<>();
            log.info(STR."\{Thread.currentThread().getName()} start whole batch insertion");
            for (int i = 0; i < 100; i++) {
                futures.add(executorService.submit(()->generateDataTask.start()));
            }
            futures.parallelStream().forEach(future -> {
                try {
                    future.get();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            });
            watch.stop();
            log.info(STR."\{Thread.currentThread().getName()} whole batch done, cost - {} ms", watch.getTotalTimeMillis());
        }catch(Exception e){
            log.error("-----> catch generateData exception  - {}", e.toString());
        }finally {
            executorService.shutdown();
            log.info(STR."\{Thread.currentThread().getName()} Executor shut down");
        }
    }

    public void importCSVFile() {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info(STR."\{Thread.currentThread().getName()} start import CSVFile");
        csvLoaderUtil.loadCSVInBatch();
        watch.stop();
        log.info(STR."\{Thread.currentThread().getName()} import CSVFile done, cost - {} ms", watch.getTotalTimeMillis());
    }
}
