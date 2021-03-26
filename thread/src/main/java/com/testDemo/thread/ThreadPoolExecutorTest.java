package com.testDemo.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: zouren
 * @date: 2021/2/23
 * @description:
 */
public class ThreadPoolExecutorTest {
    public void newThreadPoolExecutor() {

        /**
         * Creates a new {@code ThreadPoolExecutor} with the given initial
         * parameters.
         *
         * @param corePoolSize [5]核心线程数 【一直存在除非（allowCoreThreadTimeOut）】，线程池，准备就绪的线程数
         * @param maximumPoolSize [200]最大线程数,控制资源
         * @param keepAliveTime 存活时间。如果当前线程数量大于核心线程数时，释放空闲线程【maximumPoolSize-corePoolSize】.
         *                      只要线程空闲大于指定的keepAliveTime
         * @param unit 存活时间单位
         * @param workQueue 阻塞队列。如果任务有很多，就会将目前多的任务放到队列里面。
         *                  只要有线程空闲，就会去队列里面取出新的任务继续执行。
         * @param threadFactory 线程的创建工厂。
         * @param handler 如果阻塞队列满了，按照我们指定的拒绝策略拒绝执行任务
         *
         *
         *  工作顺序：
         *     1）线程池创建，准备core数量的核心线程，准备接受任务。
         *     1.1、core满了，就将再进来的任务放入阻塞队列中。空闲的core就会自己去阻塞队列获取任务执行。
         *     1.2、阻塞队列满了，就直接开新线程执行，最大只能开到max指定的数量
         *     1.3、max满了就用RejectedExecutionHandler拒绝任务。已实现的拒绝策略如下：
         *           ·AbortPolicy：该策略是线程池的默认策略。使用该策略时，如果线程池队列满了丢掉这个任务并且抛出RejectedExecutionException异常。
         *           ·DiscardOldestPolicy：这个策略从字面上也很好理解，丢弃最老的。也就是说如果队列满了，会将最早进入队列的任务删掉腾出空间，再尝试加入队列。
         *           ·CallerRunsPolicy：使用此策略，如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行。就像是个急脾气的人，我等不到别人来做这件事就干脆自己干。
         *           ·DiscardPolicy：这个策略和AbortPolicy的slient版本，如果线程池队列满了，会直接丢掉这个任务并且不会有任何异常。
         *     1.4、max都执行完成，有很多空闲，在指定的keepAliveTime以后，释放max-core线程
         *
         *               new LinkedBlockingDeque():默认是Integer的最大值。内存不够
         */

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

//        Executors.newCachedThreadPool(); core是0，所有都可以回收
//        Executors.newFixedThreadPool();固定大小 core=max,都不可回收
//        Executors.newScheduledThreadPool();定时任务的线程池
//        Executors.newSingleThreadExecutor();单线程的线程池，后台从队列里面获得任务，挨个执行
    }
}
