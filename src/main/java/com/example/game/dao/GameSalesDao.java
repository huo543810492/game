package com.example.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.game.form.GameSalesPageForm;
import com.example.game.model.GameSales;
import com.example.game.model.GameSalesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface GameSalesDao extends BaseMapper<GameSales> {

    GameSalesVO getTotalSales(@Param("saleDateGe") Date saleDateGe, @Param("saleDateLe") Date saleDateLe, @Param("gameNo") Integer gameNo);

    List<Long> selectGameSalesWithFilters(GameSalesPageForm form);
}
