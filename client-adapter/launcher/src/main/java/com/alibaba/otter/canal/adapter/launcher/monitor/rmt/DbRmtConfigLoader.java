package com.alibaba.otter.canal.adapter.launcher.monitor.rmt;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.adapter.launcher.monitor.remote.ConfigItem;
import com.alibaba.otter.canal.adapter.launcher.monitor.remote.DbRemoteConfigLoader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
public class DbRmtConfigLoader {


    private static final Logger logger = LoggerFactory.getLogger(DbRemoteConfigLoader.class);

    private final DruidDataSource dataSource;

    private final String sql =
            "select cac.id, cic.name, cac.content, cac.modified_time\n" +
                    "from canal_adapter_config cac\n" +
                    "         inner join canal_instance_config cic on cac.id = cic.id\n" +
                    "where cac.status = '0'\n";

    public DbRmtConfigLoader(String driverName, String jdbcUrl, String jdbcUsername, String jdbcPassword) {
        dataSource = new DruidDataSource();
        if (StringUtils.isEmpty(driverName)) {
            driverName = "com.mysql.jdbc.Driver";
        }
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        dataSource.setInitialSize(1);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(1);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        try {
            dataSource.init();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 加载远程application.yml配置
     */
    public List<ConfigItem> loadRemoteConfig() {
        return getRemoteAdapterConfig();
    }

    /**
     * 获取远程application.yml配置
     *
     * @return 配置对象
     */
    private List<ConfigItem> getRemoteAdapterConfig() {
        List<ConfigItem> configItemList = new LinkedList<>();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ConfigItem configItem = new ConfigItem();
                configItem.setId(rs.getLong("id"));
                configItem.setName(rs.getString("name"));
                configItem.setContent(rs.getString("content"));
                configItem.setModifiedTime(rs.getTimestamp("modified_time").getTime());
                configItemList.add(configItem);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return configItemList;
    }

}
