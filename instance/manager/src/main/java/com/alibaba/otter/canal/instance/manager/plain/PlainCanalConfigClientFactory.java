package com.alibaba.otter.canal.instance.manager.plain;

import com.alibaba.otter.canal.instance.manager.plain.db.DbBasedPlainCanalConfigClient;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.Properties;

/**
 * Created on 2021/9/4.
 *
 * @author lan
 * @since 2.0.0
 */
public class PlainCanalConfigClientFactory {

    private static volatile PlainCanalConfigClient INSTANCE;

    public static synchronized void init(Properties properties) {
        String mode = getProperty(properties, PlainCanalConfigConstants.CANAL_MANAGER_MODE);
        boolean dbMode = Objects.equals(PlainCanalConfigConstants.CANAL_MANAGER_MODE_DB, mode);
        String id = getProperty(properties, PlainCanalConfigConstants.CANAL_ID);
        boolean autoRegister = BooleanUtils.toBoolean(getProperty(properties, PlainCanalConfigConstants.CANAL_ADMIN_AUTO_REGISTER));
        String autoCluster = getProperty(properties, PlainCanalConfigConstants.CANAL_ADMIN_AUTO_CLUSTER);
        String name = getProperty(properties, PlainCanalConfigConstants.CANAL_ADMIN_REGISTER_NAME);
        String registerIp = getProperty(properties, PlainCanalConfigConstants.CANAL_REGISTER_IP);
        if (dbMode) {
            String jdbcAddress = getProperty(properties, PlainCanalConfigConstants.CANAL_MANAGER_JDBC_ADDRESS);
            int jdbcPort = Integer.parseInt(getProperty(properties, PlainCanalConfigConstants.CANAL_MANAGER_JDBC_PORT));
            String jdbcUsername = getProperty(properties, PlainCanalConfigConstants.CANAL_MANAGER_JDBC_USERNAME);
            String jdbcPassword = getProperty(properties, PlainCanalConfigConstants.CANAL_MANAGER_JDBC_PASSWORD);
            String jdbcDatabase = getProperty(properties, PlainCanalConfigConstants.CANAL_MANAGER_JDBC_DATABASE);
            INSTANCE = new DbBasedPlainCanalConfigClient(id, registerIp, autoRegister, autoCluster, name, jdbcAddress, jdbcPort, jdbcUsername, jdbcPassword, jdbcDatabase);
        } else {
            String user = getProperty(properties, PlainCanalConfigConstants.CANAL_ADMIN_USER);
            String passwd = getProperty(properties, PlainCanalConfigConstants.CANAL_ADMIN_PASSWD);
            int adminPort = Integer.parseInt(getProperty(properties, PlainCanalConfigConstants.CANAL_ADMIN_PORT, "11110"));
            String managerAddress = getProperty(properties, PlainCanalConfigConstants.CANAL_ADMIN_MANAGER);
            INSTANCE = new PlainCanalConfigClient(managerAddress, id, user, passwd, registerIp, adminPort, autoRegister, autoCluster, name);
        }
    }

    public static PlainCanalConfigClient getCanalConfigClient() {
        return INSTANCE;
    }

    private static String getProperty(Properties properties, String key, String defaultValue) {
        String value = getProperty(properties, key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }

    private static String getProperty(Properties properties, String key) {
        key = StringUtils.trim(key);
        String value = System.getProperty(key);

        if (value == null) {
            value = System.getenv(key);
        }

        if (value == null) {
            value = properties.getProperty(key);
        }

        return StringUtils.trim(value);
    }
}
