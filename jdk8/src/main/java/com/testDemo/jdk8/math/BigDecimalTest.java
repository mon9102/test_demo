package com.testDemo.jdk8.math;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * @Auther: zouren
 * @Date: 2019/3/27 17:00
 * @Description:
 */
public class BigDecimalTest {
    /**
     * @Description: 通常建议优先使用String构造方法。
     * @Author: zouren
     * @CreateDate: 2019/3/27 17:01
     */
    @Test
    public void test1(){
        BigDecimal bigDecimal = new BigDecimal(2);
        BigDecimal bDouble = new BigDecimal(2.3);
        BigDecimal bString = new BigDecimal("2.3");
        BigDecimal cDouble =  BigDecimal.valueOf(2.3);

        System.out.println( BigDecimal.valueOf(1)
                .divide(BigDecimal.valueOf(2),2,BigDecimal.ROUND_UP)

        );
        System.out.println("bigDecimal=" + bigDecimal);
        System.out.println("bDouble=" + bDouble);
        System.out.println("bString=" + bString);
        System.out.println("cDouble=" + cDouble);
    }
    @Test
    public void testadd(){
        double v1 = 1;
        double v2 = 20.2;
        double v3 = 300.03;
        double sum =  v1+v2+v3;
        System.out.println("v1+v2+v3=" +sum);

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
         sum= b1.add(b2).add(b3).doubleValue();
        System.out.println("BigDecimal v1+v2+v3=" +sum);

    }
    /**
     * @Description: 这里有一点需要注意的是除法运算divide.
     *
     *  BigDecimal除法可能出现不能整除的情况，比如 4.5/1.3，
     *  这时会报错java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
     * @Author: zouren
     * @CreateDate: 2019/3/27 17:03
     */
    @Test
    public void test2(){
        BigDecimal a = new BigDecimal("4.5");
        BigDecimal b = new BigDecimal("1.5");

        System.out.println("a + b =" + a.add(b));
        System.out.println("a - b =" + a.subtract(b));
        System.out.println("a * b =" + a.multiply(b));
        System.out.println("a / b =" + a.divide(b));
    }
    /**
     * @Description: 其实divide方法有可以传三个参数
     *
     * public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode)
     * 第一参数表示除数， 第二个参数表示小数点后保留位数，
     * 第三个参数表示舍入模式，只有在作除法运算或四舍五入时才用到舍入模式，有下面这几种
     * @Author: zouren
     * @CreateDate: 2019/3/27 17:05
     */
    @Test
    public void divideTest(){
//        BigDecimal a = new BigDecimal("4.5635");
        BigDecimal a = new BigDecimal("1.55");
        //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6
        BigDecimal b = a.setScale(1, RoundingMode.HALF_UP);
        System.out.println(b);
        double divideValue = BigDecimal.valueOf(125.5).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(divideValue);

        //向正无穷方向舍入
         b = b.setScale(3, BigDecimal.ROUND_CEILING);
        System.out.println(b);
        //向零方向舍入
         b = b.setScale(3, BigDecimal.ROUND_DOWN);
        System.out.println(b);
        //向负无穷方向舍入
         b = b.setScale(3, BigDecimal.ROUND_FLOOR);
        System.out.println(b);
        //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向下舍入, 例如1.55 保留一位小数结果为1.5
         b = b.setScale(1, BigDecimal.ROUND_HALF_DOWN);
        System.out.println(b);
        //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，如果保留位数是奇数，使用ROUND_HALF_UP，如果是偶数，使用ROUND_HALF_DOWN
         b = b.setScale(3, BigDecimal.ROUND_HALF_EVEN);
        System.out.println(b);
        //计算结果是精确的，不需要舍入模式
//         b = a.setScale(3, BigDecimal.ROUND_UNNECESSARY);
//        System.out.println(a);
        //向远离0的方向舍入
        b = b.setScale(3, BigDecimal.ROUND_UP);
        System.out.println(b);
    }
    @Test
    public void test3(){
        //测试幂运算 pow、scaleByPowerOfTen
        System.out.println(Math.pow(1.1, 2) + "  " + BigDecimal.valueOf(1.1).pow(2).doubleValue());//1.2100000000000002  1.21
        System.out.println(BigDecimal.ONE.scaleByPowerOfTen(1024));//1E+1024
        System.out.println(1.1*1.1);
    }
    /**
     * @Description: BigDecimal  divideToIntegralValue(BigDecimal divisor)  返回 BigDecimal，其值为向下舍入所得商值(this / divisor)的整数部分。该结果的首选标度为(this.scale() - divisor.scale())。
     * BigDecimal  remainder(BigDecimal divisor)  返回其值为 (this % divisor) 的 BigDecimal。余数由 this.subtract( this.divideToIntegralValue(divisor) .multiply(divisor) ) 给出。注意，这不是模操作(结果可以为负)。
     * BigDecimal[]  divideAndRemainder(BigDecimal divisor)  返回由两个元素组成的 BigDecimal 数组，该数组包含 divideToIntegralValue 的结果，后跟对两个操作数计算所得到的 remainder。注意，如果同时需要整数商和余数，则此方法比分别使用 divideToIntegralValue 和 remainder 方法更快速，因为相除仅需执行一次。
     * @Author: zouren
     * @CreateDate: 2019/3/27 17:25
     */
    @Test
    public void divideTest1(){
        BigDecimal bd1 = BigDecimal.valueOf(5);
        BigDecimal bd2 = BigDecimal.valueOf(2);
        //商
        BigDecimal bd_div = bd1.divide(bd2);
        //向下舍入所得商值(this / divisor)的整数部分
        BigDecimal bd_dti = bd1.divideToIntegralValue(bd2);
        //remainder计算的过程
        BigDecimal bd_tem = bd1.subtract(bd1.divideToIntegralValue(bd2).multiply(bd2));
        //返回其值为 (this % divisor) 的 BigDecimal。注意，这不是模操作(结果可以为负)
        BigDecimal bd_rem = bd1.remainder(bd2);
        //2.5  2  1  1  true
        System.out.println(bd_div + "  " + bd_dti + "  " + bd_tem + "  " + bd_rem + "  " + bd_tem.equals(bd_rem));
        BigDecimal[] array = bd1.divideAndRemainder(bd2);
        //[2, 1]  true  true
        System.out.println(Arrays.toString(array) + "  " + array[0].equals(bd_dti) + "  " + array[1].equals(bd_rem));
    }

    public static void main(String[] args) {
//        HSSFCell cell = null;
//        cell.setCellType(HSSFCell.ENCODING_UTF_16);


    }
}
