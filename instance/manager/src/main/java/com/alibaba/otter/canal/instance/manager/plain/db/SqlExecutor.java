package com.alibaba.otter.canal.instance.manager.plain.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.parse.driver.mysql.MysqlConnector;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created on 2021/9/4.
 *
 * @author lan
 * @since 2.0.0
 */
public class SqlExecutor {
    MysqlConnector mysqlConnector;
    MysqlConnectionPoolDataSource dataSource;

    public SqlExecutor(String username, String password, InetSocketAddress address, String database) {
        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setPassword(password);
        dataSource.setUser(username);
        dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false", address.getHostString(), address.getPort(), database));
        mysqlConnector = new MysqlConnector(new InetSocketAddress("127.0.0.1", 3306), "root", "canal_MYSQL");
        mysqlConnector.setDefaultSchema("canal_manager");
        try {
            mysqlConnector.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(String sql, Consumer<PreparedStatement> consumer) {
        try {
            PooledConnection connection = dataSource.getPooledConnection();
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            consumer.accept(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> queryMulti(String sql, Class<T> clazz) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Map<String, String>> maps = new ArrayList<>();
            while (resultSet.next()) {
                maps.add(map(resultSet));
            }
            return JSON.parseArray(JSON.toJSONString(maps), clazz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T queryOne(String sql, Class<T> clazz) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            Map<String, String> map = resultSet.next() ? map(resultSet) : new HashMap<>();
            connection.close();
            if (map.isEmpty()) {
                return null;
            }
            return JSON.parseObject(JSON.toJSONString(map), clazz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> map(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String, String> map = new HashMap<>(metaData.getColumnCount());
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnLabel(i + 1);
            String value = resultSet.getString(i + 1);
            map.put(columnName, value);
        }

        return map;
    }
}
