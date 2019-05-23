package com.sinosoft.demo2.mapper;

import com.sinosoft.demo2.dao.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {
    Product select(long id);
    void insert(long id,String name,long price);
    void delete(long id);
    void update(long id,String name,long price);
}
