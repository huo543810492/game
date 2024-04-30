package com.example.game.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class GameSalesPageForm implements Serializable{
    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleDateGe;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleDateLe;

    @Min(1)
    private int page = 1;

    @Min(1)
    private int size = 100;

    @DecimalMin(value = "0.0", inclusive = false, message = "Pram priceLessThan must be greater than 0")
    private BigDecimal priceLt;

    @DecimalMin(value = "0.0", message = "Pram priceGreaterThan must be greater than or equal 0")
    private BigDecimal priceGt;
}
