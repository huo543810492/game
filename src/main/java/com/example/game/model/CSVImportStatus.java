package com.example.game.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * game sales record
 */
@Data
@TableName(value = "csv_import_status")
public class CSVImportStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "`id`")
    private Long id;

    @TableField(value = "`start_id`")
    private Long startId;

    @TableField(value = "`end_id`")
    private Long endId;

    @TableField(value = "`create_time`")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
