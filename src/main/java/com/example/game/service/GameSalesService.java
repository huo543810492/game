package com.example.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.game.form.GameTotalSalesForm;
import com.example.game.model.GameSales;
import com.example.game.form.GameSalesPageForm;
import com.example.game.model.GameSalesVO;

public interface GameSalesService extends IService<GameSales> {

    IPage<GameSales> getGameSales(GameSalesPageForm form);

    GameSalesVO getTotalSales(GameTotalSalesForm form);

}
