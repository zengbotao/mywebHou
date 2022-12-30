package com.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("tbl_md")
public class Md implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String scan;
    private String name;
    private String label;
    private String description;
    private String text;
    private String title;
    private Timestamp time;
}
