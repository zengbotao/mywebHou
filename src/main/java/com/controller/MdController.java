package com.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapper.MdMapper;
import com.model.*;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CacheConfig(cacheNames = "md")
@RestController
@Transactional
@RequestMapping("/md")
public class MdController {
    @Autowired
    private MdMapper mdMapper;

    @Autowired
    //打包page参数
    private PageFunc pageFunc;

    @PostMapping("/upload")
    public Result save(@RequestBody Md md) {
        int flag = mdMapper.insert(md);
        Boolean status=flag==1;
        return new Result(status ? Code.SAVE_OK:Code.SAVE_ERR,status);
    }
    @GetMapping("/{id}")
    @Cacheable(value = "md", key = "#name+':'+#id")
    public Result getById(@PathVariable Long id){
        Md md=mdMapper.selectById(id);
        Integer code = md != null ? Code.GET_OK : Code.GET_ERR;
        String msg = md != null ? "" : "数据查询失败，请重试！";
        return new Result(code,md,msg);
    }
    //    条件分页查询
    @GetMapping("/Wrapper")
    public  Result Wrapper(
            @RequestParam(value="Scan",required = false) String Scan,
            @RequestParam(value="Label",required = false) String Label,
            @RequestParam(value="Title",required = false) String Title,
            @RequestParam(value="PageCur") Integer PageCur,
            @RequestParam(value="PageSize") Integer PageSize
    ){
        IPage<Md> page=new Page<>(PageCur,PageSize);
        LambdaQueryWrapper<Md> lqw = new LambdaQueryWrapper<Md>();
        lqw.eq(!StringUtils.isEmpty(Scan), Md::getScan, Scan);
        lqw.eq(!StringUtils.isEmpty(Label),Md::getLabel, Label);
        lqw.like(!StringUtils.isEmpty(Title),Md::getTitle, Title);
        lqw.select(Md::getId,Md::getTitle, Md::getDescription);
        mdMapper.selectPage(page,lqw);
        List<Md> md=page.getRecords();
        PageParam pageParam =pageFunc.getpageParam(page.getSize(),page.getCurrent(),page.getTotal());
        PageFen data=pageFunc.getPageFen(md,pageParam);
        Integer code = md != null ? Code.GET_OK : Code.GET_ERR;
        String msg = md != null ? "" : "数据查询失败，请重试！";
        return new Result(code,data,msg);
    }


}


