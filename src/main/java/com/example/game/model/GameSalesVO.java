package com.example.game.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * game sales record
 */
@Data
public class GameSalesVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer totalSalesNo;

    private BigDecimal totalSalesAmount = BigDecimal.ZERO;
}
