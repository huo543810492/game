package com.example.game.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * game sales record
 */
@Data
public class GameSalesVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer totalSalesNo;

    private BigDecimal totalSalesAmount = BigDecimal.ZERO;
}
