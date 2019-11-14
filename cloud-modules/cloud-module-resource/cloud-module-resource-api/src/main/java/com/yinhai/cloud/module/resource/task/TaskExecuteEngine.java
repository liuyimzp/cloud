package com.yinhai.cloud.module.resource.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author: zhaokai
 * @create: 2018-08-28 11:59:07
 */
public class TaskExecuteEngine {

    private final static Logger logger = LoggerFactory.getLogger(TaskExecuteEngine.class);
    private static ExecutorService pool ;

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("task-pool-%d").build();
        pool=   new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    }
    public static void execute(AbstractOperateTask task) {

        try {
            if (task.needCheck()) {
                task.initCheck();
            }
            if (!task.beforeExecute()) {
                return;
            }

        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
            task.executeFailed(e);
            return;
        }
        pool.execute(() -> {
            try {
                task.onExecute();
                task.successfullyExecuted();
            } catch (Exception e) {
                logger.error(logger.getName() + "context",e);
                task.executeFailed(e);
            }
        });
    }
}
