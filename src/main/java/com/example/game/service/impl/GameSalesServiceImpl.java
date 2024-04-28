package com.example.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.game.dao.GameSalesDao;
import com.example.game.model.GameSales;
import com.example.game.service.GameSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Service
public class GameSalesServiceImpl extends ServiceImpl<GameSalesDao, GameSales> implements GameSalesService {

    @Autowired
    GameSalesDao gameSalesDao;

    @Override
    public IPage<GameSales> getGameSales() {
        QueryWrapper<GameSales> wrapper = new QueryWrapper<>();
        wrapper.in("game_no", 1);
        GameSales gameSales1 = new GameSales();
        gameSales1.setId(2L);
        gameSales1.setGameName("aa");
        gameSales1.setGameCode("aa");
        gameSales1.setGameNo(2);
        gameSales1.setDate(new Date());
        gameSales1.setCreate_time(new Date());
        gameSales1.setSalePrice(new BigDecimal(3.3));
        gameSales1.setType(1);
        gameSales1.setCostPrice(new BigDecimal(3.4));
        gameSales1.setTax(new BigDecimal(3.4));
        gameSalesDao.insert(gameSales1);

        Page<GameSales> pageBean = new Page<>(1, 100);
        pageBean.setRecords(new ArrayList<>(pageBean.getRecords()));
        pageBean.setSize(pageBean.getSize());
        pageBean.setTotal(pageBean.getTotal());
        pageBean.setCurrent(pageBean.getCurrent());
        return null;
    }
}
