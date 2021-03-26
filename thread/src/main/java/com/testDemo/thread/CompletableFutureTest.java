package com.testDemo.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zouren
 * @date: 2021/2/23
 * @description:
 * CompletableFuture方法使用
 * 线程串行化
 *   1）thenRun: 不能获取上一步的执行结果
 *   2）thenAccept: 能获取上一步的执行结果,但无返回值
 *   3）thenApply: 能获取上一步的执行结果,也有返回值
 * 两个任务组合--都要完成才能执行
 *   1）thenCombine: 组合两个future,获取两个future的返回值，并返回当前任务的返回值
 *   2）thenAccepteBoth: 组合两个future,获取两个future的返回值，然后处理任务，没有返回值
 *   3）runAfterBoth: 组合两个future,不需要两个future的返回值，只需要两个future任务完成之后，处理该任务
 * 两个任务组合-- 只一个任务完成就执行
 *   1）applyToEither: 两个任务有一个执行完成,获取它的返回值，处理任务并有新的返回值
 *   2）acceptEither: 两个任务有一个执行完成,获取它的返回值，处理任务,没有返回值
 *     两任务需要相同返回类型
 *   3）runAfterEither: 两个任务有一个执行完成,不需要任务返回值，处理任务,没有返回值
 * 多任务组合
 *   1）allOf: 等待所有任务完成
 *   2）anyOf: 只要有一个任务完成
 *
 */
public class CompletableFutureTest {
    /**
     * 线程串行化
     *  1）thenRun: 不能获取上一步的执行结果
     * @throws Exception
     */
    @Test
    public void thenRun() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->{
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("运行结果："+i);
            return i;
        },executorService).thenRunAsync(()->{
            System.out.println("启动线程2");
        });
        System.out.println("完成");
    }

    /**
     * 线程串行化
     *  1）thenAccept: 能获取上一步的执行结果,但无返回值
     * @throws Exception
     */
    @Test
    public void thenAccept() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->{
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("运行结果："+i);
            return i;
        },executorService).thenAcceptAsync(res->{
            System.out.println("启动线程2:"+res);
        });
        System.out.println("完成");
    }
    /**
     * 线程串行化
     *  1）thenApply: 能获取上一步的执行结果,也有返回值
     * @throws Exception
     */
    @Test
    public void thenApply() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->{
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("运行结果："+i);
            return i;
        },executorService).thenApplyAsync(res->{
            int a = res*2;
            System.out.println("启动线程2:"+a);
            return a;
        });


        System.out.println("完成"+future.get());
    }
    /**
     * 两个任务组合--都要完成才能执行
     *  1）thenCombine: 组合两个future,获取两个future的返回值，并返回当前任务的返回值
     * @throws Exception
     */
    @Test
    public void thenCombine() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(()->{
            System.out.println("任务1线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("任务1结束："+i);
            return i;
        },executorService);

        CompletableFuture<String> future2 =CompletableFuture.supplyAsync(()->{
            System.out.println("任务2线程："+Thread.currentThread().getId());
            System.out.println("任务2结束：");

            return "hello";
        },executorService);
        CompletableFuture<String> future3 = future1.thenCombineAsync(future2, (f1, f2) -> {
            //入参为future1返回值  future2返回值
            System.out.println("任务3开始。。。之前的结果:" + f1 + "-----" + f2);
            //最终的返回的值
            return "任务3" + f1 + "-----" + f2;
        }, executorService);

        System.out.println("完成:"+future3.get());
    }
    /**
     * 两个任务组合--都要完成才能执行
     *  1）thenAccepteBoth: 组合两个future,获取两个future的返回值，然后处理任务，没有返回值
     * @throws Exception
     */
    @Test
    public void thenAccepteBoth() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(()->{
            System.out.println("任务1线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("任务1结束："+i);
            return i;
        },executorService);

        CompletableFuture<String> future2 =CompletableFuture.supplyAsync(()->{
            System.out.println("任务2线程："+Thread.currentThread().getId());
            System.out.println("任务2结束：");

            return "hello";
        },executorService);

        future1.thenAcceptBothAsync(future2,(f1,f2)->{
            //入参为future1返回值  future2返回值
            System.out.println("任务3开始。。。之前的结果:"+f1+"-----"+f2);
        },executorService);
        System.out.println("完成");
    }
    /**
     * 两个任务组合--都要完成才能执行
     *  1）runAfterBoth: 组合两个future,不需要两个future的返回值，只需要两个future任务完成之后，处理该任务
     * @throws Exception
     */
    @Test
    public void runAfterBoth() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(()->{
            System.out.println("任务1线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("任务1结束："+i);
            return i;
        },executorService);

        CompletableFuture<String> future2 =CompletableFuture.supplyAsync(()->{
            System.out.println("任务2线程："+Thread.currentThread().getId());
            System.out.println("任务2结束：");

            return "hello";
        },executorService);
        future1.runAfterBothAsync(future2,()->{
            System.out.println("任务3开始。。。");
        },executorService);

        System.out.println("完成");
    }
    /**
     * 两个任务组合-- 只一个任务完成就执行
     *  1）applyToEither: 两个任务有一个执行完成,获取它的返回值，处理任务并有新的返回值
     * @throws Exception
     */
    @Test
    public void applyToEither() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(()->{
            System.out.println("任务1线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("任务1结束："+i);
            return i;
        },executorService);

        CompletableFuture<Object> future2 =CompletableFuture.supplyAsync(()->{
            System.out.println("任务2线程："+Thread.currentThread().getId());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2结束：");

            return "hello";
        },executorService);
        CompletableFuture<Object> future3 = future1.applyToEitherAsync(future2, (res) -> {
            System.out.println("任务3开始。。。");
            return res;
        }, executorService);

        System.out.println("完成:"+future3.get());
    }
    /**
     * 两个任务组合-- 只一个任务完成就执行
     *  1）acceptEither: 两个任务有一个执行完成,获取它的返回值，处理任务,没有返回值
     *  两任务需要相同返回类型
     * @throws Exception
     */
    @Test
    public void acceptEither() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(()->{
            System.out.println("任务1线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("任务1结束："+i);
            return i;
        },executorService);

        CompletableFuture<Object> future2 =CompletableFuture.supplyAsync(()->{
            System.out.println("任务2线程："+Thread.currentThread().getId());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务2结束：");

            return "hello";
        },executorService);
        //两任务需要相同返回类型
        future1.acceptEitherAsync(future2,(res)->{

            System.out.println("任务3开始。。。:"+res);
        },executorService);

        System.out.println("完成");
    }
    /**
     * 两个任务组合-- 只一个任务完成就执行
     *  1）runAfterEither: 两个任务有一个执行完成,不需要任务返回值，处理任务,没有返回值
     * @throws Exception
     */
    @Test
    public void runAfterEither() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(()->{
            System.out.println("任务1线程："+Thread.currentThread().getId());
            int i = 10/4;
            System.out.println("任务1结束："+i);
            return i;
        },executorService);

        CompletableFuture<String> future2 =CompletableFuture.supplyAsync(()->{
            System.out.println("任务2线程："+Thread.currentThread().getId());
            System.out.println("任务2结束：");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        },executorService);
        future1.runAfterEitherAsync(future2,()->{
            System.out.println("任务3开始。。。");
        },executorService);

        System.out.println("完成");
    }
    /**
     * 多任务组合
     *  1）allOf: 等待所有任务完成
     * @throws Exception
     */
    @Test
    public void allOf() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{

            return "商品信息";
        },executorService);

        CompletableFuture<String> future2 =CompletableFuture.supplyAsync(()->{
            return "商品图片";
        },executorService);
        CompletableFuture<String> future3 =CompletableFuture.supplyAsync(()->{
            return "商品属性";
        },executorService);
        CompletableFuture<Void> future = CompletableFuture.allOf(future1, future2,future3);
        future.get();//等待所有线程做完
        System.out.println("完成"+future1.get()+"--"+future2.get()+"---"+future3.get());
    }
    /**
     * 多任务组合
     *  1）anyOf: 只要有一个任务完成
     * @throws Exception
     */
    @Test
    public void anyOf() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{

            return "商品信息";
        },executorService);

        CompletableFuture<String> future2 =CompletableFuture.supplyAsync(()->{
            return "商品图片";
        },executorService);
        CompletableFuture<String> future3 =CompletableFuture.supplyAsync(()->{
            return "商品属性";
        },executorService);
        CompletableFuture<Object> future = CompletableFuture.anyOf(future1, future2,future3);
        //有一个完成就返回
        System.out.println("完成:"+future.get());
    }
}
