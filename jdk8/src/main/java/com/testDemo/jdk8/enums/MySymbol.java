package com.testDemo.jdk8.enums;

import lombok.Getter;

/**
 *  0外汇
 * 1黄金
 * 2原油
 * 3白银
 * @return
*/
@Getter
public enum MySymbol {
    /**0外汇**/
    WAIHUI(0),
    /**1黄金**/
    HUANGJIN(1),
    /**2原油**/
    YUANYOU(2),
    /**3白银**/
    BAIYIN(3);
    private int value ;
    private MySymbol(int value){
        this.value = value;
    }

    public static void main(String[] args) {

        System.out.println( MySymbol.valueOf("WAIHUI").value);
        System.out.println(MySymbol.BAIYIN.equals(MySymbol.BAIYIN));
        System.out.println(MySymbol.BAIYIN==MySymbol.BAIYIN);
    }
}
