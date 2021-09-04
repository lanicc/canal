package com.alibaba.otter.canal.instance.manager.plain.db;

import com.alibaba.otter.canal.parse.driver.mysql.MysqlConnector;
import org.junit.Test;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

/**
 * Created on 2021/9/4.
 *
 * @author lan
 * @since 2.0.0
 */
public class SqlExecutorTest {

    @Test
    public void insert() {
        //SqlExecutor executor = new SqlExecutor("root", "canal_MYSQL", new InetSocketAddress("127.0.0.1", 3306), "canal_manager");
        //String sql = "insert into canal_config(id, cluster_id, server_id, name, status, content, content_md5, modified_time)\n" +
        //        "VALUES (null, null, 3, 'test', '1', '', '',  now())\n";
        //executor.insert(sql);
    }
}
