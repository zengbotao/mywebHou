package com.model;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PageFunc {
    //合并分页与结果列表
    public PageFen getPageFen(List data,PageParam page){
        PageFen result=new PageFen();
        result.setData(data);
        result.setPage(page);
        return  result;
    }
    //合并分页参数
    public static PageParam getpageParam( Long pageSize, Long pageCur, Long pageTotal){
        PageParam pageParam =new PageParam();
        pageParam.setPageCur(pageCur);
        pageParam.setPageTotal(pageTotal);
        pageParam.setPageSize(pageSize);
        return pageParam;
    }
}
