package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.model.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
