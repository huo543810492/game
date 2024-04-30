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
        ThreadFactory factory = Thread.ofVirtual().name("my-virtual-thread-", 1).factory();
        ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            List<Future<?>> futures = new ArrayList<>();
            log.info("{} start whole batch insertion", Thread.currentThread().getName());
            for (int i = 0; i < 100; i++) {
                futures.add(executor.submit(()->generateDataTask.start()));
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
            log.info("{} whole batch done, cost - {} ms",Thread.currentThread().getName(), watch.getTotalTimeMillis());
        }catch(Exception e){
            log.error("-----> catch generateData exception  - {}", e.toString());
        }finally {
            executor.shutdown();
            log.info("{} Executor shut down", Thread.currentThread().getName());
        }
    }

    public void importCSVFile() {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("{} start import CSVFile", Thread.currentThread().getName());
        csvLoaderUtil.loadCSVInBatch();
        watch.stop();
        log.info("{} import CSVFile done, cost - {} ms",Thread.currentThread().getName(), watch.getTotalTimeMillis());
    }
}
