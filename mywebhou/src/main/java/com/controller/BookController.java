package com.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapper.BookMapper;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@CacheConfig(cacheNames = "book")
@RestController
@Transactional
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookMapper bookMapper;

    @Autowired
    //打包page参数
    private PageFunc pageFunc;
    @PostMapping("/upload")
    public Result save(@RequestBody Book md) {
        int flag = bookMapper.insert(md);
        Boolean status=flag==1;
        return new Result(status ? Code.SAVE_OK:Code.SAVE_ERR,status);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "book", key = "#name+':'+#id")
    public Result getById(@PathVariable Long id){
        Book md=  bookMapper.selectById(id);
        Integer code = md != null ? Code.GET_OK : Code.GET_ERR;
        String msg = md != null ? "" : "数据查询失败，请重试！";
        return new Result(code,md,msg);
    }
    @PostMapping("/update")
    public Result updateById(
            @RequestBody Book book
    ){
        int  md = bookMapper.updateById(book);
        Integer code = md != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = md != 0 ? "" : "数据查询失败，请重试！";
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
        IPage<Book> page=new Page<>(PageCur,PageSize);
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<Book>();
        lqw.eq(!StringUtils.isEmpty(Scan), Book::getScan, Scan);
        lqw.eq(!StringUtils.isEmpty(Label),Book::getLabel, Label);
        lqw.like(!StringUtils.isEmpty(Title),Book::getTitle, Title);
        lqw.select(Book::getId,Book::getTitle, Book::getDescription);
        bookMapper.selectPage(page,lqw);
        List<Book> md=page.getRecords();
        PageParam pageParam =pageFunc.getpageParam(page.getSize(),page.getCurrent(),page.getTotal());
        PageFen data=pageFunc.getPageFen(md,pageParam);
        Integer code = md != null ? Code.GET_OK : Code.GET_ERR;
        String msg = md != null ? "" : "数据查询失败，请重试！";
        return new Result(code,data,msg);
    }
    //    条件分页查询
    @GetMapping("/new")
    public  Result Wrapperall(){
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<>();
        lqw.select(Book::getId,Book::getTitle);
        List<Book> md=bookMapper.selectList(lqw);
        int len=md.size();
        md= md.subList(len-11,len-1);
        System.out.println(md.size());
        Integer code = md != null ? Code.GET_OK : Code.GET_ERR;
        String msg = md != null ? "" : "数据查询失败，请重试！";
        return new Result(code,md,msg);
    }
}
