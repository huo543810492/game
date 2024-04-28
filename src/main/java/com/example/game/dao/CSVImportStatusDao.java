package com.example.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.game.model.CSVImportStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CSVImportStatusDao extends BaseMapper<CSVImportStatus> {
}
