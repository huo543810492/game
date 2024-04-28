package com.example.game.service.impl;

import com.example.game.Util.CSVLoaderUtil;
import com.example.game.service.GameSalesDataService;
import com.example.game.task.GenerateDataTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

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
        StopWatch watch = new StopWatch();
        watch.start();
        log.info(Thread.currentThread().getName() + " start whole batch insertion");
        for (int i = 0; i < 100; i++) {
            generateDataTask.start();
        }
        watch.stop();
        log.info(Thread.currentThread().getName() + " whole batch done, cost - {} ms" , watch.getTotalTimeMillis());
    }

    public void importCSVFile() {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info(Thread.currentThread().getName() + " start import CSVFile");
        csvLoaderUtil.loadCSVInBatch();
        watch.stop();
        log.info(Thread.currentThread().getName() + " import CSVFile done, cost - {} ms" , watch.getTotalTimeMillis());
    }
}
