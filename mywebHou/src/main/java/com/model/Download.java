package com.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("downlist")
public class Download implements Serializable {
    private Long id;
    private String description;
    private String title;
    private Timestamp time;
}
