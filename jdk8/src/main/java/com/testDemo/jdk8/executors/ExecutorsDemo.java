package com.testDemo.jdk8.executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author : zouren
 * @date : 2020/4/3 10:30
 */
public class ExecutorsDemo {
    /**
     * 避免使用Executors创建线程池，主要是避免使用其中的默认实现，
     * 那么我们可以自己直接调用ThreadPoolExecutor的构造函数来自己创建线程池。
     * 在创建的同时，给BlockQueue指定容量就可以了。
     * 这种情况下，一旦提交的线程数超过当前可用线程数时，
     * 就会抛出java.util.concurrent.RejectedExecutionException，
     * 这是因为当前线程池使用的队列是有边界队列，队列已经满了便无法继续处理新的请求。
     * 但是异常（Exception）总比发生错误（Error）要好。
     */
    public static ExecutorService executor = new ThreadPoolExecutor(10, 10,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));

    /**
     * ArrayBlockingQueue是一个用数组实现的有界阻塞队列，必须设置容量。
     *
     * LinkedBlockingQueue是一个用链表实现的有界阻塞队列，容量可以选择进行设置，不设置的话，将是一个无边界的阻塞队列，最大长度为Integer.MAX_VALUE。
     */

    /**
     * 用guava提供的ThreadFactoryBuilder来创建线程池
     */
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();
    public static ExecutorService pool = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

}
