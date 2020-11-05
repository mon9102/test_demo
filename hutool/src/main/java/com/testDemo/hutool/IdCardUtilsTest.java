package com.testDemo.hutool;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdcardUtil;
import org.junit.jupiter.api.Test;

/**
 * @author: zouren
 * @date: 2020/8/17 17:24
 * @description:
 */
public class IdCardUtilsTest {
   static String ID_18 = "321083197812162119";
    static String ID_15 = "150102880730303";
    @Test
    public void getBirth(){

        System.out.println(  IdcardUtil.getBirthByIdCard(ID_18));
        System.out.println(IdcardUtil.getBirthDate(ID_18).toDateStr());
        System.out.println(IdcardUtil.getBirth(ID_18));
    }
    @Test
    public void getGenderByIdCard (){
        //性别(1 : 男 ， 0 : 女)
        System.out.println(  IdcardUtil.getGenderByIdCard(ID_18) );

    }
    public static void main(String[] args) {


//是否有效
        boolean valid = IdcardUtil.isValidCard(ID_18);
        boolean valid15 = IdcardUtil.isValidCard(ID_15);

//转换
        String convert15To18 = IdcardUtil.convert15To18(ID_15);
//        Assert.assertEquals(convert15To18, "150102198807303035");

//年龄
        DateTime date = DateUtil.parse("2017-04-10");

        int age = IdcardUtil.getAgeByIdCard(ID_18, date);
//        Assert.assertEquals(age, 38);

        int age2 = IdcardUtil.getAgeByIdCard(ID_15, date);
//        Assert.assertEquals(age2, 28);

//生日
        String birth = IdcardUtil.getBirthByIdCard(ID_18);
//        Assert.assertEquals(birth, "19781216");

        String birth2 = IdcardUtil.getBirthByIdCard(ID_15);
//        Assert.assertEquals(birth2, "19880730");

//省份
        String province = IdcardUtil.getProvinceByIdCard(ID_18);
//        Assert.assertEquals(province, "江苏");

        String province2 = IdcardUtil.getProvinceByIdCard(ID_15);
//        Assert.assertEquals(province2, "内蒙古");
    }
}
