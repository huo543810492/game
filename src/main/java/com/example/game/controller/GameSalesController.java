package com.example.game.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.game.model.GameSales;
import com.example.game.model.GameSalesPageForm;
import com.example.game.service.GameSalesDataService;
import com.example.game.service.GameSalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/gameSales")
@RestController
@Slf4j
public class GameSalesController {

    @Autowired
    GameSalesService gameSalesService;

    @Autowired
    GameSalesDataService gameSalesDataService;

    @PostMapping ("/getGameSales")
    public IPage<GameSales> page(@RequestBody GameSalesPageForm form){
        StopWatch watch = new StopWatch();
        watch.start();
        log.info(Thread.currentThread().getName() + " start query getGameSales");
        IPage<GameSales> page = gameSalesService.getGameSales(form);
        watch.stop();
        log.info(Thread.currentThread().getName() + " query getGameSales done, cost - {} ms" , watch.getTotalTimeMillis());
        return page;
    }

    @GetMapping ("/generatorData")
    public void generatorData(){
        gameSalesDataService.generateData();
    }

    @GetMapping ("/import")
    public void importCSVFile(){
        gameSalesDataService.importCSVFile();
    }
}
