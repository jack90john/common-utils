package com.jack90john.common.utils.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *   多任务批量执行工具
 * @author jack
 * @version 1.1.1
 * @since 1.1.0.RELEASE
 */
public class ThreadUtil {

    /*常用时间间隔 start*/
    public static final Integer A_MINUTE = 60000;               //一分钟
    public static final Integer HALF_OF_A_MINUTE = 30000;       //半分钟
    public static final Integer TEN_SECOND = 10000;             //十秒
    public static final Integer A_SECOND = 1000;                //一秒
    public static final Integer HARF_OF_A_SECOND = 500;         //500毫秒
    public static final Integer A_HUNDRED_MILLISECONDS = 100;   //100毫秒
    public static final Integer FIFTY_MILLISECONDS = 50;        //50毫秒
    public static final Integer TEN_MILLISECONDS = 10;          //10毫秒
    /*常用时间间隔 end*/

    private static final ThreadUtil threadUtil = new ThreadUtil();

    private ThreadUtil() {
    }

    public static ThreadUtil getInstance() {
        return threadUtil;
    }

    /**
     * 提交并等待结果
     *
     * @param executor  执行线程池
     * @param solvers   待执行线程
     * @param timeout   超时时间，单位ms
     * @param sleepTime 捕获结果间隔时间，单位ms（间隔不可设置太小，如果太小易造成cpu占用100%）
     * @param <T>       返回类型
     * @return 结果List
     * @since 1.1.0.RELEASE
     */
    public <T> List<T> submitAndWait(Executor executor, List<Callable<T>> solvers, Integer timeout, Integer sleepTime) {
        List<T> returnList = new ArrayList<>();
        int n = solvers.size();
        int count = 0;
        long waitCnt = 0;
        CompletionService<T> completionService = new ExecutorCompletionService<>(executor);
        solvers.forEach(completionService::submit);
        while (count < n && waitCnt < timeout) {
            try {
                TimeUnit.MILLISECONDS.sleep(sleepTime);
                waitCnt += sleepTime;
                for (int i = count; i < n; ++i) {
                    Future<T> poll = completionService.poll();
                    if (poll != null) {
                        T t = poll.get();
                        returnList.add(t);
                        count++;
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }

}
