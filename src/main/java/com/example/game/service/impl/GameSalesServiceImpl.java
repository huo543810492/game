package com.example.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.game.dao.GameSalesDao;
import com.example.game.model.GameSales;
import com.example.game.model.GameSalesPageForm;
import com.example.game.service.GameSalesService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSalesServiceImpl extends ServiceImpl<GameSalesDao, GameSales> implements GameSalesService {

    @Autowired
    GameSalesDao gameSalesDao;

    @Override
    public IPage<GameSales> getGameSales(GameSalesPageForm form) {
        Page<GameSales> page = new Page<>(form.getPage(), form.getSize());
        QueryWrapper<GameSales> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(ObjectUtils.isNotEmpty(form.getSaleDateGe()),"date_of_sale",form.getSaleDateGe());
        queryWrapper.le(ObjectUtils.isNotEmpty(form.getSaleDateLe()),"date_of_sale",form.getSaleDateLe());
        queryWrapper.gt(ObjectUtils.isNotEmpty(form.getPriceGt()),"cost_price",form.getPriceGt());
        queryWrapper.lt(ObjectUtils.isNotEmpty(form.getPriceLt()),"cost_price",form.getPriceLt());

        return gameSalesDao.selectPage(page, queryWrapper);
    }
}
