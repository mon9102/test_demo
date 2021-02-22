package com.test;

/**
 * @author: zouren
 * @date: 2021/2/22
 * @description:
 * 766. 托普利茨矩阵
 */
public class Solution {
    public static boolean isToeplitzMatrix(int[][] matrix) {
        //高
        int m = matrix.length;
        //宽
        int n = matrix[0].length;

        int x = m - 1;
        int y = 0;
        //斜角值
        int row = matrix[x][y];
        int topleft = x;
        int topright = 0;
        while (topleft >= 0 && topright < n) {
            row = matrix[topleft][topright];
            x = topleft;
            y = topright;
            while (x < m && y < n) {
                if (matrix[x][y] == row) {
                    row = matrix[x][y];
                    x++;
                    y++;
                } else {
                    return false;
                }
            }
            if(topleft>0){
                topleft--;
            }else {
                topright++;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int [] matrix[] ={{1,2,3,4},{5,1,2,3},{9,5,1,2}};
        System.out.println( isToeplitzMatrix(matrix));
        int [] matrix1[] ={{1,2},{2,2}};
        System.out.println( isToeplitzMatrix(matrix1));
    }
}
