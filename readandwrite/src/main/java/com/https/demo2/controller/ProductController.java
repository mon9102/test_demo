package com.https.demo2.controller;

import com.https.demo2.dao.Product;
import com.https.demo2.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/select/{id}")
    public void getProduct(@PathVariable("id") Long productId){
        Product product = productMapper.select(productId);
        System.out.println(product);
    }

    @GetMapping("/insert/{id}/{name}/{price}")
    public void insertProduct(@PathVariable("id") Long productId,@PathVariable("name") String name,@PathVariable("price") Long price){
        productMapper.insert(productId,name,price);
    }
    @GetMapping("/delete/{id}")
    public void deleteProduct(@PathVariable("id") Long productId){
        productMapper.delete(productId);
    }

    @GetMapping("/update/{id}/{name}/{price}")
    public void updateProduct(@PathVariable("id") Long productId,@PathVariable("name") String name,@PathVariable("price") Long price){
        productMapper.update(productId,name,price);
    }
}
