package com.yinhai.modules.codetable.domain.bpo;

import com.yinhai.core.common.api.base.IConstants;
import com.yinhai.core.common.api.config.impl.SysConfig;
import com.yinhai.modules.codetable.api.domain.bpo.IAppCodeBpo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author aolei 2017年3月22日10:35:42
 *         修改加载方法为bean初始化init 销毁destory
 */
public class CodeTableInitService {

    private static Log logger = LogFactory.getLog(CodeTableInitService.class);

    private static final String lockPath = "/codeTableCacheLock";

    private IAppCodeBpo appCodeBpo;

    public CodeTableInitService() {
        //empty constructor
    }

    public IAppCodeBpo getAppCodeBpo() {
        return appCodeBpo;
    }

    public void setAppCodeBpo(IAppCodeBpo appCodeBpo) {
        this.appCodeBpo = appCodeBpo;
    }

    public void init() {
        //启动是否加载码表
        boolean cacheCodeTable = SysConfig.getSysConfigToBoolean("cacheCodeTable", true);
        if (cacheCodeTable) {
            initAppCode();
            if (SysConfig.getSysConfigToBoolean("isConfigDistribute", false)) {
                initWithLock();
            }
        }
    }

    private void initWithLock() {
        CuratorFramework client= CuratorFrameworkFactory
                .newClient(SysConfig.getSysConfig("configRegisterUrl"),
                        new ExponentialBackoffRetry(1000, 3));
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        try {
            lock.acquire();
//            initAppCode();
        } catch (Exception e) {
            logger.error("代码表插件加载失败...", e);
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                logger.error("释放码表缓存分布式锁失败...", e);
            }
        }
    }

    private void initAppCode() {
        logger.info("开始启动代码表的插件！");
        appCodeBpo.reloadAll(false);
        logger.info("代码表插件启动完成");
    }
}
