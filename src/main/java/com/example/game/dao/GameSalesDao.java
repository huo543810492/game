package com.example.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.game.model.GameSales;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameSalesDao extends BaseMapper<GameSales> {
    List<GameSales> selectList();
}
