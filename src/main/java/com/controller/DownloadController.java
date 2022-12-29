package com.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapper.DownloadMapper;
import com.mapper.MdMapper;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CacheConfig(cacheNames = "download")
@RestController
@Transactional
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    private DownloadMapper downloadMapper;

    @Autowired
    //打包page参数
    private PageFunc pageFunc;
    @PostMapping("/upload")
    public Result save(@RequestBody Download md) {
        int flag = downloadMapper.insert(md);
        Boolean status=flag==1;
        return new Result(status ? Code.SAVE_OK:Code.SAVE_ERR,status);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "download", key = "#name+':'+#id")
    public Result getById(@PathVariable Long id){
        Download md=downloadMapper.selectById(id);
        Integer code = md != null ? Code.GET_OK : Code.GET_ERR;
        String msg = md != null ? "" : "数据查询失败，请重试！";
        return new Result(code,md,msg);
    }

    //    条件分页查询
    @GetMapping("/Wrapper")
    public  Result Wrapper(
            @RequestParam(value="Title",required = false) String Title,
            @RequestParam(value="PageCur") Integer PageCur,
            @RequestParam(value="PageSize") Integer PageSize
    ){
        IPage<Download> page=new Page<>(PageCur,PageSize);
        LambdaQueryWrapper<Download> lqw = new LambdaQueryWrapper<Download>();
        lqw.like(!StringUtils.isEmpty(Title),Download::getTitle, Title);
        downloadMapper.selectPage(page,lqw);
        List<Download> md=page.getRecords();
        PageParam pageParam =pageFunc.getpageParam(page.getSize(),page.getCurrent(),page.getTotal());
        PageFen data=pageFunc.getPageFen(md,pageParam);
        Integer code = md != null ? Code.GET_OK : Code.GET_ERR;
        String msg = md != null ? "" : "数据查询失败，请重试！";
        return new Result(code,data,msg);
    }

}
