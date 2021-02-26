package com.test;

import org.junit.jupiter.api.Test;

/**
 * @author: zouren
 * @date: 2021/2/22
 * @description:
 *
 */
public class Solution {
    /**
     * 766. 托普利茨矩阵
     * @param matrix
     * @return
     */
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
    public void isToeplitzMatrixTest(){
        int [] matrix[] ={{1,2,3,4},{5,1,2,3},{9,5,1,2}};
        System.out.println( isToeplitzMatrix(matrix));
        int [] matrix1[] ={{1,2},{2,2}};
        System.out.println( isToeplitzMatrix(matrix1));
    }
    /**
     * 寻找两个正序数组的中位数
     * @param nums1
     * @param nums2
     * @return
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int l = nums1.length+nums2.length;
        boolean a = l%2==0;
        int w = 0;
        int i=0,j=0;

        int emp = 0,emp1 = 0,emp2 = 0;
        int re = 0;
        if(a){
            w=l/2+1;
        }else {
            w=(l+1)/2;
        }
        while(w>0){
            if(a&&w==1){
                re = emp;
            }
            if(nums1.length>0&&i<nums1.length){
                emp1 = nums1[i];
            }
            if(nums2.length>0&&j<nums2.length){
                emp2 = nums2[j];
            }
             if(i>=nums1.length){
                 if(j<nums2.length){
                     emp =nums2[j];
                     j++;
                 }
            }else if(j>=nums2.length){
                 if(i<nums1.length){
                     emp =nums1[i];
                     i++;
                 }
            }else if(emp2>=emp1){
                emp = emp1;
                i++;
                emp = Math.min(emp,Math.min(emp1,emp2));
            }else if(emp2<emp1){
                emp = emp2;
                j++;
                emp = Math.min(emp,Math.min(emp1,emp2));
            }
           w--;
        }
        if(a){
            return (re+emp+0.0)/2;
        }else {
            return emp;
        }
    }
    @Test
    public void findMedianSortedArraysTest(){
        int [] nums1 ={1,3};
        int [] nums2 ={2,4};
        System.out.println( findMedianSortedArrays(nums1,nums2));
    }

    /**
     * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
     *
     * '.' 匹配任意单个字符
     * '*' 匹配零个或多个前面的那一个元素
     * 所谓匹配，是要涵盖 整个 字符串 s的，而不是部分字符串。
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch(String s, String p) {
        if(s!=null&&p!=null){
            int pl = p.length();
            int w = 0;
            char[]ss = s.toCharArray();
            String[] ps = p.split("\\*");
            for (int i = 0; i < ps.length; i++) {
               if(ps[i].length()==1){
                   for (int j = 0; j < ss.length; j++) {
                       if(ps[i].equals(ss[j])){
                           w++;
                       }
                   }
               }
            }
        }
        return false;
    }
    @Test
    public void isMatchTest(){
        String s = "mississippi" ,p = "mis*is*p*.";
        System.out.println(isMatch(s,p));
    }
    public static void main(String[] args) {
    String p = "mis*is*p*.*";
    String[] ps = p.split("\\*");
        System.out.println(ps);
    }
}
