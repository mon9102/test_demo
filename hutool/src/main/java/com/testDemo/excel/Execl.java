package com.testDemo.excel;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @auther: zouren
 * @date: 2020/12/29
 * @description:
 */
public class Execl {
    @Test
    public void test(){
        PrimaryKeyUtil primaryKeyUtil = new PrimaryKeyUtil();
        String sql ="update vip_supplier set dr=3,PROVINCE='%s',CITY='%s',DISTRICT=%s where id='%s';";
        ExcelReader reader = ExcelUtil.getReader("d:/bbbb.xlsx");
        ExcelReader vipsupplier = ExcelUtil.getReader("d:/update-supplier-to0.xlsx");
        Map<String,List<Object>> map = vipsupplier.read().stream().collect(Collectors.toMap( (e) -> (String) e.get(3),
                (e) -> e));
        List<List<Object>> readAll = reader.read(0);
        AtomicInteger i= new AtomicInteger();
        List<String> l = new ArrayList();
        readAll.stream().forEach(row->{
            String code = row.get(2).toString();
            List<Object> old = map.get(code);
            String p = Optional.ofNullable(row.get(4)).map(s->s.toString()).orElse("");
            String c = Optional.ofNullable(row.get(5)).map(s->s.toString()).orElse("");
            String state = row.get(9).toString();
            if(state.equals("无效")||p.length()>0){
                String a = null;
                try {
                    a = Optional.ofNullable(row.get(6)).map(s->s.toString()).orElse("");
                    if(a.length()>0){
                        a ="'"+ a+"'";
                    }else {
                        a = "null";
                    }
                } catch (Exception e) {
                    a = "null";
                }
                l.add(old.get(0).toString());
                    i.getAndIncrement();
                    System.out.println(String.format(sql, p ,c,a,old.get(0).toString()));

            }


        });
        System.out.println(i.get());
        System.out.println("======");
        l.stream().forEach(row-> System.out.print(",'"+row+"'"));
        System.out.println();

    }

}
