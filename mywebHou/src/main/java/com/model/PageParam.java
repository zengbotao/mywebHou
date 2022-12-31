package com.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
    private Long pageSize;
    private Long pageTotal;
    private Long pageCur;
}
