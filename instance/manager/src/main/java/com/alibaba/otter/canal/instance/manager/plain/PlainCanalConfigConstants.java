package com.alibaba.otter.canal.instance.manager.plain;

/**
 * Created on 2021/9/4.
 *
 * @author lan
 * @since 2.0.0
 */
public class PlainCanalConfigConstants {
    public static final String ROOT = "canal";

    public static final String CANAL_MANAGER_MODE = ROOT + "." + "manager.mode";//db、admin
    public static final String CANAL_MANAGER_MODE_DB = "db";
    public static final String CANAL_MANAGER_MODE_ADMIN = "admin";
    public static final String CANAL_MANAGER_JDBC_USERNAME = ROOT + "." + "manager.jdbc.username";
    public static final String CANAL_MANAGER_JDBC_PASSWORD = ROOT + "." + "manager.jdbc.password";
    public static final String CANAL_MANAGER_JDBC_ADDRESS = ROOT + "." + "manager.jdbc.address";
    public static final String CANAL_MANAGER_JDBC_PORT = ROOT + "." + "manager.jdbc.port";
    public static final String CANAL_MANAGER_JDBC_DATABASE = ROOT + "." + "manager.jdbc.database";


    public static final String CANAL_ID                             = ROOT + "." + "id";
    public static final String CANAL_IP                             = ROOT + "." + "ip";
    public static final String CANAL_REGISTER_IP                    = ROOT + "." + "register.ip";
    public static final String CANAL_PORT                           = ROOT + "." + "port";
    public static final String CANAL_USER                           = ROOT + "." + "user";
    public static final String CANAL_PASSWD                         = ROOT + "." + "passwd";

    public static final String CANAL_ADMIN_MANAGER                  = ROOT + "." + "admin.manager";
    public static final String CANAL_ADMIN_PORT                     = ROOT + "." + "admin.port";
    public static final String CANAL_ADMIN_USER                     = ROOT + "." + "admin.user";
    public static final String CANAL_ADMIN_PASSWD                   = ROOT + "." + "admin.passwd";
    public static final String CANAL_ADMIN_AUTO_REGISTER            = ROOT + "." + "admin.register.auto";
    public static final String CANAL_ADMIN_AUTO_CLUSTER             = ROOT + "." + "admin.register.cluster";
    public static final String CANAL_ADMIN_REGISTER_NAME            = ROOT + "." + "admin.register.name";
}
