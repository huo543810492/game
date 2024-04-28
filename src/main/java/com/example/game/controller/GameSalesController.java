package com.example.game.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.game.model.GameSales;
import com.example.game.service.GameSalesDataService;
import com.example.game.service.GameSalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/gameSales")
@RestController
@Slf4j
public class GameSalesController {

    @Autowired
    GameSalesService gameSalesService;

    @Autowired
    GameSalesDataService gameSalesDataService;

    @GetMapping ("/page")
    public IPage<GameSales> page(){
        IPage<GameSales> page = gameSalesService.getGameSales();
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
