package com.example.game.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.game.form.GameTotalSalesForm;
import com.example.game.model.GameSales;
import com.example.game.form.GameSalesPageForm;
import com.example.game.model.GameSalesVO;
import com.example.game.service.GameSalesDataService;
import com.example.game.service.GameSalesService;
import com.example.game.support.ResultMsg;
import jakarta.validation.Valid;
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
    public ResultMsg page(@RequestBody @Valid GameSalesPageForm form){
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            log.info("{} start query getGameSales", Thread.currentThread().getName());
            IPage<GameSales> page = gameSalesService.getGameSales(form);
            watch.stop();
            log.info("{} query getGameSales done, cost - {} ms",Thread.currentThread().getName(), watch.getTotalTimeMillis());
            return ResultMsg.success(page);
        }catch (Exception e){
            return ResultMsg.failure(e.getMessage());
        }
    }

    @GetMapping ("/generatorData")
    public ResultMsg generatorData(){
        try {
            gameSalesDataService.generateData();
            return ResultMsg.success();
        }catch (Exception e){
            return ResultMsg.failure(e.getMessage());
        }
    }

    @GetMapping ("/import")
    public ResultMsg importCSVFile(){
        try {
            gameSalesDataService.importCSVFile();
            return ResultMsg.success();
        }catch (Exception e){
            return ResultMsg.failure(e.getMessage());
        }
    }

    @PostMapping ("/getTotalSales")
    public ResultMsg getTotalSales(@RequestBody @Valid GameTotalSalesForm form){
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            log.info("{} start query getTotalSales", Thread.currentThread().getName());
            GameSalesVO totalSalesVO = gameSalesService.getTotalSales(form);
            watch.stop();
            log.info("{} query getTotalSales done, cost - {} ms",Thread.currentThread().getName(), watch.getTotalTimeMillis());
            return ResultMsg.success(totalSalesVO);
        }catch (Exception e){
            return ResultMsg.failure(e.getMessage());
        }
    }
}
