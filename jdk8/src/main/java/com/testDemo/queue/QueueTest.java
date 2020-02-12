package com.testDemo.queue;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Auther: zouren
 * @Date: 2020/2/6 10:42
 * @Description:
 */
public class QueueTest {
    private Queue<String> queue = new LinkedList<String>();

    public static void main(String[] args) {
        System.out.println();

    }
    @Test
    public void test1(){
        System.out.println("test1");
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        String emp = null;
        int len = queue.size();
        int curLen = 0;
        while ((emp=queue.poll())!=null&&curLen<=len){
            System.out.println(emp+"   curLen="+ curLen+"  len="+len);
            curLen++;
            queue.offer(emp);
        }
        System.out.println("111");
        len = queue.size();
        curLen = 0;
        System.out.println("curLen="+ curLen+"  len="+len);
        while ((emp=queue.poll())!=null&&curLen<=len){
            System.out.println("222=="+emp);
            curLen++;
            queue.offer(emp);
        }
    }
}
