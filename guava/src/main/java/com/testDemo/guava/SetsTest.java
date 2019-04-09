package com.testDemo.guava;

import com.google.common.collect.Sets;

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;

/**
 * @Auther: zouren
 * @Date: 2019/4/9 14:50
 * @Description:
 */
public class SetsTest {
    public static void main(String[] args) {

       	HashSet<Integer> setA = newHashSet(1, 2, 3, 4, 5);
       	HashSet<Integer> setB = newHashSet(4, 5, 6, 7, 8);

       	Sets.SetView<Integer> union = Sets.union(setA, setB);
       	System.out.println("union:");
       	for (Integer integer : union)
             System.out.println(integer);

       	Sets.SetView<Integer> difference = Sets.difference(setA, setB);
       	System.out.println("difference:");
       	for (Integer integer : difference)
            	    System.out.println(integer);

       	Sets.SetView<Integer> intersection = Sets.intersection(setA, setB);
       	System.out.println("intersection:");
       	for (Integer integer : intersection)
            	    System.out.println(integer);
    }
}
