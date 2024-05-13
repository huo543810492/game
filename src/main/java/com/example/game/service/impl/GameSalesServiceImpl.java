package com.example.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.game.dao.GameSalesDao;
import com.example.game.form.GameTotalSalesForm;
import com.example.game.model.GameSales;
import com.example.game.form.GameSalesPageForm;
import com.example.game.model.GameSalesVO;
import com.example.game.service.GameSalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.List;

@Slf4j
@Service
public class GameSalesServiceImpl extends ServiceImpl<GameSalesDao, GameSales> implements GameSalesService {

    @Autowired
    GameSalesDao gameSalesDao;

    @Override
    public List<GameSales> getGameSales(GameSalesPageForm form) {
        int page = form.getPage();
        form.setPage((page-1)*form.getSize());
        Assert.isTrue(form.getPage()>=0,"Param page num is not correct");
//        Page<GameSales> page = new Page<>(form.getPage(), form.getSize());
//        QueryWrapper<GameSales> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ge(ObjectUtils.isNotEmpty(form.getSaleDateGe()),"date_of_sale",form.getSaleDateGe());
//        queryWrapper.le(ObjectUtils.isNotEmpty(form.getSaleDateLe()),"date_of_sale",form.getSaleDateLe());
//        queryWrapper.gt(ObjectUtils.isNotEmpty(form.getPriceGt()),"cost_price",form.getPriceGt());
//        queryWrapper.lt(ObjectUtils.isNotEmpty(form.getPriceLt()),"cost_price",form.getPriceLt());
//        gameSalesDao.selectPage(page, queryWrapper);
        StopWatch selectWithFiltersWatch = new StopWatch();
        selectWithFiltersWatch.start();
        List<Long> gameSales = gameSalesDao.selectGameSalesWithFilters(form);
        selectWithFiltersWatch.stop();
        log.info("selectGameSalesWithFilters cost - {} ms", selectWithFiltersWatch.getTotalTimeMillis());
        log.info("according pagination get ids, {}", gameSales.toString());
        StopWatch selectBatchIds = new StopWatch();
        selectBatchIds.start();
        List<GameSales> result = gameSalesDao.selectBatchIds(gameSales);
        selectBatchIds.stop();
        log.info("selectBatchIds cost - {} ms", selectBatchIds.getTotalTimeMillis());
        return result;
    }

    @Override
    public GameSalesVO getTotalSales(GameTotalSalesForm form) {
        GameSalesVO totalSales = gameSalesDao.getTotalSales(form.getSaleDateGe(), form.getSaleDateLe(), form.getGameNo());
        return totalSales;
    }
}
