package com.example.game.task;

import com.example.game.model.GameSales;
import com.example.game.service.GameSalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class GenerateDataTask {

    @Autowired
    GameSalesService gameSalesService;

    @Async
    public void start() {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info(Thread.currentThread().getName() + " start batch insertion");
        final int DEFAULT_BATCH_SIZE = 10000;
        List<GameSales> batch = new ArrayList<>();
        Random random = new Random();
        // one batch roughly cost 1 second
        for (int i = 0; i < DEFAULT_BATCH_SIZE; i++) {
            GameSales salesRecord = new GameSales();
            salesRecord.setGameNo(random.nextInt(100) + 1);
            salesRecord.setGameName("test");
            salesRecord.setGameCode("code");
            salesRecord.setType(random.nextInt(2) + 1);
            salesRecord.setCostPrice(new BigDecimal(random.nextInt(100) + 1));
            salesRecord.setTax(new BigDecimal("9.00"));
            salesRecord.setSalePrice(new BigDecimal(random.nextInt(100) + 1));
            salesRecord.setDate(generateRandomDateInApril());
            salesRecord.setCreate_time(new Date());

            batch.add(salesRecord);
        }
        watch.stop();
        log.info(Thread.currentThread().getName() + " one batch done, cost " + watch.getTotalTimeMillis() + " ms");
        gameSalesService.saveBatch(batch);
    }

    private Date generateRandomDateInApril() {
        Random random = new Random();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2024); // 设置年份为2024年
        calendar.set(Calendar.MONTH, Calendar.APRIL); // 四月份
        calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(30) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(24));
        calendar.set(Calendar.MINUTE, random.nextInt(60));
        calendar.set(Calendar.SECOND, random.nextInt(60));
        calendar.set(Calendar.MILLISECOND, random.nextInt(1000));
        return calendar.getTime();
    }
}
