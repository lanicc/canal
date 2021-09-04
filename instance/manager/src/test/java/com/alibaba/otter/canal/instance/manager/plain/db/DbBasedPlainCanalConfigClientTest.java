package com.alibaba.otter.canal.instance.manager.plain.db;

import com.alibaba.otter.canal.instance.manager.plain.PlainCanal;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.List;

/**
 * Created on 2021/9/4.
 *
 * @author lan
 * @since 2.0.0
 */
public class DbBasedPlainCanalConfigClientTest {
    SqlExecutor executor;

    @Before
    public void setUp() throws Exception {
        executor = new SqlExecutor("root", "canal_MYSQL", new InetSocketAddress("127.0.0.1", 3306), "canal_manager");
    }

    @Test
    public void findServer() throws SQLException, IOException {
        List<PlainCanal> plainCanals = executor.queryMulti("select * from canal_node_server", PlainCanal.class);
        System.out.println(plainCanals);
    }


}
