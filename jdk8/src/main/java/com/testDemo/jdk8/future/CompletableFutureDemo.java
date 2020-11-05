package com.testDemo.jdk8.future;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : zouren
 * @date : 2020/4/3 09:28
 */
public class CompletableFutureDemo {
    /**
     * 在Java8中，CompletableFuture提供了非常强大的Future的扩展功能，可以帮助我们简化异步编程的复杂性，
     * 并且提供了函数式编程的能力，可以通过回调的方式处理计算结果，也提供了转换和组合 CompletableFuture 的方法
     * <p>
     * 注意: 方法中有Async一般表示另起一个线程,没有表示用当前线程
     */
    @Test
    public void test01() throws Exception {

        ExecutorService service = Executors.newFixedThreadPool(5);
        /**
         *  supplyAsync用于有返回值的任务，
         *  runAsync则用于没有返回值的任务
         *  Executor参数可以手动指定线程池，否则默认ForkJoinPool.commonPool()系统级公共线程池
         */
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "future";
        }, service);
        CompletableFuture<Void> data = CompletableFuture.runAsync(() -> System.out.println("data.runAsync"));
        /**
         * 计算结果完成回调
         */
        future.whenComplete((x, y) -> System.out.println("future.whenComplete x=" + x + ",y=" + y)); //执行当前任务的线程执行继续执
        data.whenCompleteAsync((x, y) -> System.out.println("data.whenCompleteAsync x=" + x + ",y=" + y)); // 交给线程池另起线程执行
        future.exceptionally(Throwable::toString);
        //System.out.println(future.get());
        /**
         * thenApply,一个线程依赖另一个线程可以使用,出现异常不执行
         */
        //第二个线程依赖第一个的结果
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 5).thenApply(x -> x);

        /**
         * handle 是执行任务完成时对结果的处理,第一个出现异常继续执行
         */
        CompletableFuture<Integer> future2 = future1.handleAsync((x, y) -> x + 2);
        System.out.println("future2.get=" + future2.get());//7
        /**
         * thenAccept 消费处理结果,不返回
         */
        future2.thenAccept(System.out::println);
        /**
         * thenRun  不关心任务的处理结果。只要上面的任务执行完成，就开始执行
         */
        future2.thenRunAsync(() -> System.out.println("future2.thenRunAsync  继续下一个任务"));
        /**
         * thenCombine 会把 两个 CompletionStage 的任务都执行完成后,两个任务的结果交给 thenCombine 来处理
         */
        CompletableFuture<Integer> future3 = future1.thenCombine(future2, Integer::sum);
        CompletableFuture<Integer> futureTest = future1.thenCombine(future2,(x,y)->x*y);
        System.out.println("futureTest=" + futureTest.get()); // 5*7=35

        System.out.println("future3=" + future3.get()); // 5+7=12
        /**
         * thenAcceptBoth : 当两个CompletionStage都执行完成后，把结果一块交给thenAcceptBoth来进行消耗
         */
        future1.thenAcceptBothAsync(future2, (x, y) -> System.out.println("future1.thenAcceptBothAsync x=" + x + ",y=" + y)); //5,7
        /**
         * applyToEither
         * 两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的转化操作
         */
        CompletableFuture<Integer> future4 = future1.applyToEither(future2, x -> x);
        System.out.println("future4.get()=" + future4.get()); //5
        /**
         * acceptEither
         * 两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的消耗操作
         */
        future1.acceptEither(future2, (x) -> System.out.println("future1.acceptEither,x=" + x));
        /**
         * runAfterEither
         * 两个CompletionStage，任何一个完成了都会执行下一步的操作（Runnable
         */
        future1.runAfterEither(future, () -> System.out.println("future1.runAfterEither 有一个完成了,我继续"));
        /**
         * runAfterBoth
         * 两个CompletionStage，都完成了计算才会执行下一步的操作（Runnable）
         */
        future1.runAfterBoth(future, () -> System.out.println("future1.runAfterBoth 都完成了,我继续"));
        /**
         * thenCompose 方法
         * thenCompose 方法允许你对多个 CompletionStage 进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作
         * thenApply是接受一个函数,thenCompose是接受一个future实例,更适合处理流操作
         */
        future1.thenComposeAsync(x -> CompletableFuture.supplyAsync(() -> x + 1))
                .thenComposeAsync(x -> CompletableFuture.supplyAsync(() -> x + 2))
                .thenCompose(x -> CompletableFuture.runAsync(() -> System.out.println("流操作结果:" + x)));
        TimeUnit.SECONDS.sleep(5);//主线程sleep,等待其他线程执行
    }


    /**
     * 我们可以通过 thenCombine()方法整合两个异步计算的结果，注意，以下代码的整个程序过程是同步的，
     * getNow()方法最终会输出整合后的结果，也就是说大写字符和小写字符的串联值。
     */
    @Test
    public void thenCombineExample() throws Exception {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApply(s -> delayedUpperCase(s))
                .thenCombine(CompletableFuture.completedFuture(original).thenApply(s -> delayedLowerCase(s)),
                (s1, s2) -> s1 + s2);
        //MESSAGEmessage
        System.out.println(cf.getNow(null));
    }

    /**
     * 上面这个示例是按照同步方式执行两个方法后再合成字符串，以下代码采用异步方式同步执行两个方法，
     * 由于异步方式情况下不能够确定哪一个方法最终执行完毕，所以我们需要调用 join()方法等待后一个方法结束后再合成字符串，
     * 这一点和线程的 join()方法是一致的，主线程生成并起动了子线程，如果子线程里要进行大量的耗时的运算，
     * 主线程往往将于子线程之前结束，但是如果主线程处理完其他的事务后，需要用到子线程的处理结果，
     * 也就是主线程需要等待子线程执行完成之后再结束，这个时候就要用到 join()方法了，即 join()的作用是："等待该线程终止"。
     */
    @Test
    public void thenCombineAsyncExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> delayedUpperCase(s))
                .thenCombine(CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedLowerCase(s)),(s1, s2) -> s1 + s2);
   assertEquals("MESSAGEmessage", cf.join());
        System.out.println(cf.getNow(null));
    }

    /**
     * 除了 thenCombine()方法以外，还有另外一种方法-thenCompose()，
     * 这个方法也会实现两个方法执行后的返回结果的连接。
     * thenCompose同步没有区别
     */
    @Test
    public void thenComposeExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedUpperCase(s))
                .thenCompose(upper -> CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedLowerCase(s))
                        .thenApply(s -> upper + s));
        assertEquals("MESSAGEmessage", cf.join());
        System.out.println(cf.getNow(null));
    }

    /**
     * 模拟了如何在几个计算过程中任意一个完成后创建 CompletableFuture，
     * 在这个例子中，我们创建了几个计算过程，然后转换字符串到大写字符。
     * 由于这些 CompletableFuture 是同步执行的（下面这个例子使用的是 thenApply()方法，而不是 thenApplyAsync()方法），
     * 使用 anyOf()方法后返回的任何一个值都会立即触发 CompletableFuture。
     * 然后我们使用 whenComplete(BiConsumer<? super Object, ? super Throwable> action)方法处理结果。
     */
    @Test
    public void anyOfExample(){
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());
        CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((res, th) -> {
            if(th == null) {
                result.append(res);
            }
        });
        System.out.println( result);
    }

    /**
     * 我们会以同步方式执行多个异步计算过程，在所有计算过程都完成后，创建一个 CompletableFuture。
     */
    @Test
    public void allOfExample(){
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((v, th) -> {
            futures.forEach(cf -> result.append(cf.getNow(null)));
            result.append("done");
        });
        System.out.println( result);
    }
    /**
     *我们会以异步步方式执行多个异步计算过程，在所有计算过程都完成后，创建一个 CompletableFuture。
     */
    @Test
    public void allOfAsyncExample(){
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());
        CompletableFuture allOf =  CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((v, th) -> {
            futures.forEach(cf -> result.append(cf.getNow(null)));
            result.append("done");
        });
        allOf.join();
        System.out.println( result);
    }

    private static String delayedUpperCase(String s){
        try {
            System.out.println(11111);
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return s.toUpperCase();
    }
    private static String delayedLowerCase(String s){
        System.out.println(222);

        return s.toLowerCase();
    }

}
