package com.testDemo.guava.future;

import com.google.common.util.concurrent.*;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author : zouren
 * @date : 2020/4/29 14:25
 */
public class ListenableFutureTest {
    /**
     * <pre>
     * 1.更多执行者
     * 该类是final类型的工具类，提供了很多静态方法。诸如ListeningDecorator方法初始化ListeningExecutorService方法，使用此实例提交方法即可初始化ListenableFuture对象。
     * 2. ListeningExecutorService
     * 该类是对ExecutorService的扩展，重新执行ExecutorService类中的提交方法，返回ListenableFuture对象。
     *
     * 3. ListenableFuture
     * 该接口扩展了Future接口，增加了addListener方法，该方法在给定的执行者上注册一个监听器，当计算完成时会马上调用该监听器。不能确保监听器执行的顺序，但可以在计算完成时确保马上被调用。
     *
     * 4. FutureCallback
     * 该接口提供了OnSuccess和OnFailure方法。获取逐步计算的结果并进行。
     *
     * 5.期货
     * 该类提供了很多实用的静态方法以供实用。
     *
     * 6. ListenableFutureTask
     * 该类扩展了FutureTask类并实现了ListenableFuture接口，增加了addListener方法。
     * </pre>
     */
    @Test
    public void futureTest(){
        // 创建线程池
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        Long t1 = System.currentTimeMillis();
        // 任务1
        ListenableFuture<Boolean> booleanTask = service.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                return true;
            }
        });
        Futures.addCallback(booleanTask, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                System.err.println("BooleanTask: " + result);
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });

        // 任务2
        ListenableFuture<String> stringTask = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello World";
            }
        });
        Futures.addCallback(stringTask, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.err.println("StringTask: " + result);
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });

        // 任务3
        ListenableFuture<Integer> integerTask = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt(100);
            }
        });
        Futures.addCallback(integerTask, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println("IntegerTask: " + result);
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
        // 执行时间
        System.err.println("time: " + (System.currentTimeMillis() - t1));

    }

    /**
     * 举一个生活上的例子，假如我们需要出去旅游，需要完成三个任务：
     *
     * 任务一：订购航班
     * 任务二：订购酒店
     * 任务三：订购租车服务
     * 很显然任务一和任务二没有相关性，可以单独执行。但是任务三必须等待任务一与任务二结束之后，才能订购租车服务。
     *
     * 为了使任务三时执行时能获取到任务一与任务二执行结果，我们还需要借助 CountDownLatch 。
     */
    @Test
    public void futureTest2() throws Exception{
        // 创建线程池
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        CountDownLatch countDownLatch = new CountDownLatch(2);
        //任务1 订购航班
        ListenableFuture<String> orderAriplane  = service.submit(()->{
            System.out.println("查航班");
            Thread.sleep(3000);
            System.out.println("订航班");
            countDownLatch.countDown();
            return "航班号";
        });

        //任务2 订购酒店
        ListenableFuture<String> orderHotel  = service.submit(()->{
            System.out.println("查酒店");
            Thread.sleep(5000);
            System.out.println("订酒店");
            countDownLatch.countDown();
            return "酒店号";
        });
        //等待 1与2 完成
        countDownLatch.countDown();
        //任务3 行程
        ListenableFuture<String> orderCar  = service.submit(()->{
            System.out.println("查行程 查车");
            System.out.println(orderHotel.get()+" ||| "+orderAriplane.get());
            Thread.sleep(5000);
            return "车号";
        });
        System.out.println("end:"+orderCar.get());
    }



    public static void main(String[] args) throws Exception {

    }
}
