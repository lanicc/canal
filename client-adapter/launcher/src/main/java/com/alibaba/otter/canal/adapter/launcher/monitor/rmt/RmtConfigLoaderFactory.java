package com.alibaba.otter.canal.adapter.launcher.monitor.rmt;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * 远程配置装载器工厂类
 *
 * @author rewerma 2019-01-25 下午05:20:16
 * @version 1.0.0
 */
public class RmtConfigLoaderFactory {

    private static final Logger logger = LoggerFactory.getLogger(RmtConfigLoaderFactory.class);

    public static DbRmtConfigLoader getRemoteConfigLoader(Environment env) {
        try {
            String jdbcUrl = env.getProperty("canal.manager.jdbc.url");
            if (!StringUtils.isEmpty(jdbcUrl)) {
                // load remote config
                String driverName = env.getProperty("canal.manager.jdbc.driverName");
                String jdbcUsername = env.getProperty("canal.manager.jdbc.username");
                String jdbcPassword = env.getProperty("canal.manager.jdbc.password");
                return new DbRmtConfigLoader(driverName, jdbcUrl, jdbcUsername, jdbcPassword);
            }
            // 可扩展其它远程配置加载器
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
