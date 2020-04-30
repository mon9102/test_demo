package com.https.demo2.mapper;

import com.https.demo2.dao.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    Product select(long id);
    void insert(long id,String name,long price);
    void delete(long id);
    void update(long id,String name,long price);
}
