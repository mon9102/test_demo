package com.testDemo.utils;

import com.testDemo.jdk8.enums.MySymbol;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @Auther: zouren
 * @Date: 2020/1/10 17:33
 * @Description:
 */
public class BeanUtilsTest {
    @Test
    public void test1()throws Exception{
        Mt4Rule mt4Rule = new Mt4Rule();
        mt4Rule.setjBaiyin(1D);
        mt4Rule.setzBaiyin(2D);
        String str = MySymbol.BAIYIN.name().toLowerCase();

        System.out.println(str);
        System.out.println(  StringUtils.join("z",str.substring(0,1).toUpperCase(),str.substring(1)));
        System.out.println(PropertyUtils.getSimpleProperty(mt4Rule,"zbaiyin"));
    }
}
