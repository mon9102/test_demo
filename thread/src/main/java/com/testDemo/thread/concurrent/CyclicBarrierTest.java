package com.testDemo.thread.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Auther: zouren
 * @Date: 2019/3/15 11:00
 * @Description:
 */
public class CyclicBarrierTest {
    /**
     * 假若有若干个线程都要进行写数据操作，并且只有所有线程都完成写数据操作之后，这些线程才能继续做后面的事情，此时就可以利用CyclicBarrier了
     */
    public void cyclicBarrier1(){
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++)
            new Writer(barrier).start();
        System.out.println("===========end=============");
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }

    /**
     * 　　如果说想在所有线程写入操作完之后，进行额外的其他操作可以为CyclicBarrier提供Runnable参数：
     */
    public  void cyclicBarrier2(){
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N,new Runnable() {

            public void run() {
                System.out.println("当前线程"+Thread.currentThread().getName());
            }
        });

        for(int i=0;i<N;i++)
            new Writer(barrier).start();

    }

    public static void main(String[] args) {
        CyclicBarrierTest cyclicBarrierTest = new CyclicBarrierTest();
        cyclicBarrierTest.cyclicBarrier2();
    }
}
