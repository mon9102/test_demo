package com.testDemo.guava;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Map;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 14:19
 * @Description: 使用Table可以实现二维矩阵的数据结构，可以是稀溜矩阵
 */
public class TableTest {
    /**
     * 示例中我们通过HashBasedTable创建了一个行类型为Integer，列类型也为Integer，值为String的Table。
     * 然后我们使用put方法向Table中添加了一些值，然后显示这些值
     * @param args
     */
    public static void main(String[] args) {
        Table<Integer, Integer, String> table = HashBasedTable.create();
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 5; column++) {
                table.put(row, column, "value of cell (" + row + "," + column + ")");
            }
        }
        for (int row=0;row<table.rowMap().size();row++) {
            Map<Integer,String> rowData = table.row(row);
            for (int column =0;column < rowData.size(); column ++) {
                System.out.println("cell(" + row + "," + column + ") value is:" + rowData.get(column));
            }
        }
    }
}
