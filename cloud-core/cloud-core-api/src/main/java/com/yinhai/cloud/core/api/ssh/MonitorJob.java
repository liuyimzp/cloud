package com.yinhai.cloud.core.api.ssh;

import com.yinhai.modules.schedul.domain.manager.ClusterJob;
import com.yinhai.modules.schedul.domain.manager.LocalJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.Serializable;
import java.time.Duration;

/**
 * @Author ：kangdw
 * @Date : 2019/3/7
 */
@DisallowConcurrentExecution
public class MonitorJob extends LocalJob implements Serializable {

    private static final long serialVersionUID = 7439699693039534612L;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Duration maxLiveDuration = Duration.ofSeconds(1);
        SSHSessionPool.closeNotUsedSession(maxLiveDuration);
    }

}
