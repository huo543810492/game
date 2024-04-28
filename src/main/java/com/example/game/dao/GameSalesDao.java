package com.example.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.game.model.GameSales;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Date;
import java.util.Map;

@Mapper
public interface GameSalesDao extends BaseMapper<GameSales> {
}
