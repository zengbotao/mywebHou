package com.model;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Data
public class PageFen implements Serializable {
    private List<Md> data;
    private PageParam page;

}

