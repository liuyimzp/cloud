package com.yinhai.cloud.module.resource.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author: liuyi02
 * @create: 2019-05-30
 */
public class TaskDownloadExecuteEngine {

    private final static Logger logger = LoggerFactory.getLogger(TaskDownloadExecuteEngine.class);
    private static ExecutorService poolDown ;

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("task-pool-%d").build();
        poolDown=   new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    }
    public static void execute(AbstractOperateTask task) {

        try {
            if (!task.beforeExecute()) {
                return;
            }

        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
            task.executeFailed(e);
            return;
        }
        poolDown.execute(() -> {
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
