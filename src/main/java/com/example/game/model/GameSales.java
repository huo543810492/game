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
@TableName(value = "game_sales")
public class GameSales implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "`id`")
    private Long id;

    @TableField(value = "`game_no`")
    private Integer gameNo;

    @TableField(value = "`game_name`")
    private String gameName;

    @TableField(value = "`game_code`")
    private String gameCode;

    @TableField(value = "`type`")
    private Integer type;

    @TableField(value = "`cost_price`")
    private BigDecimal costPrice;

    @TableField(value = "`tax`")
    private BigDecimal tax;

    @TableField(value = "`sale_price`")
    private BigDecimal salePrice;

    @TableField(value = "`date_of_sale`")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date date;

    @TableField(value = "`create_time`")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date create_time;
}
