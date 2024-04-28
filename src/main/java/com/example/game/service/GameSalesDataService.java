package com.example.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.game.model.GameSales;

public interface GameSalesDataService {
    void generateData();

    void importCSVFile();
}
