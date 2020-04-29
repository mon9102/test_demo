package com.testDemo.future;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : zouren
 * @date : 2020/4/29 15:04
 */
public class FutureTest {

    /**
     * 举一个生活上的例子，假如我们需要出去旅游，需要完成三个任务：
     *
     * 任务一：订购航班
     * 任务二：订购酒店
     * 任务三：订购租车服务
     * 很显然任务一和任务二没有相关性，可以单独执行。但是任务三必须等待任务一与任务二结束之后，才能订购租车服务。
     * CompletableFuture实现
     *  CompletableFuture 优点在于：
     *
     * 不需要手工分配线程，JDK 自动分配
     * 代码语义清晰，异步任务链式调用
     * 支持编排异步任务
     */
    @Test
    public void completableFutureTest ()throws Exception{
        // 创建线程池
        ExecutorService service = Executors.newFixedThreadPool(10);

        //任务1 订购航班
        CompletableFuture<String> orderAriplane  = CompletableFuture.supplyAsync(()->{
            System.out.println("查航班");
            sleep(3000);
            System.out.println("订航班");
            return "航班号";
        },service);
        //不在方法后面设置 线程池时 ，默认将会使用公共的 ForkJoinPool 线程池执行，这个线程池默认线程数是 CPU 的核数。所以需要自定义线程来使用。
        //任务2 订购酒店
        CompletableFuture<String> orderHotel  = CompletableFuture.supplyAsync(()->{
            System.out.println("查酒店");
            sleep(5000);
            System.out.println("订酒店");
            return "酒店号";
        },service);

        //任务3 行程
        CompletableFuture<String> orderCar  = orderHotel.thenCombine(orderAriplane,(hotel,ariplane)->{
            System.out.println("查行程 查车");
            System.out.println(hotel+"|||| "+ariplane);
            sleep(5000);
            return "车号";
        });
        System.out.println("end:"+orderCar.join());
    }
    static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
