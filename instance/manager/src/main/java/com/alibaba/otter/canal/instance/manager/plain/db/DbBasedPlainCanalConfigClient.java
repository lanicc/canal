package com.alibaba.otter.canal.instance.manager.plain.db;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.instance.manager.plain.PlainCanal;
import com.alibaba.otter.canal.instance.manager.plain.PlainCanalConfigClient;
import com.alibaba.otter.canal.protocol.SecurityUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Created on 2021/9/4.
 *
 * @author lan
 * @since 2.0.0
 */
public class DbBasedPlainCanalConfigClient extends PlainCanalConfigClient {

    private SqlExecutor sqlExecutor;

    public DbBasedPlainCanalConfigClient(String id, String localIp, boolean autoRegister, String autoCluster, String name,
                                         String jdbcAddress, int jdbcPort, String jdbcUsername, String jdbcPassword, String jdbcDatabase) {
        super(null, id, null, null, localIp, 0, autoRegister, autoCluster, name);
        this.sqlExecutor = new SqlExecutor(jdbcUsername, jdbcPassword, new InetSocketAddress(jdbcAddress, jdbcPort), jdbcDatabase);
    }

    private static final String CLASSPATH_URL_PREFIX = "classpath:";


    @Override
    public PlainCanal findServer(String md5) {
        PlainCanal plainCanal = null;
        plainCanal = sqlExecutor.queryOne(String.format("select content, content_md5 as md5, status from canal_config where server_id = %s", id), PlainCanal.class);
        if (plainCanal == null && autoRegister) {
            String conf = System.getProperty("canal.conf", "classpath:canal.properties");
            InputStream inputStream = null;
            String content = "";
            String md5Str = "";
            try {
                if (conf.startsWith(CLASSPATH_URL_PREFIX)) {
                    conf = StringUtils.substringAfter(conf, CLASSPATH_URL_PREFIX);
                    inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(conf);
                } else {
                    inputStream = new FileInputStream(conf);
                }
                if (inputStream != null) {
                    content = IOUtils.toString(inputStream);
                    md5Str = SecurityUtil.md5String(content);
                }
            } catch (IOException ignored) {

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }


            String finalContent = content;
            String finalMd5Str = md5Str;
            sqlExecutor.insert(
                    "insert into canal_config(id, cluster_id, server_id, name, status, content, content_md5, modified_time) VALUES (null, null, ?, ?, '', ?, ?,  now())",
                    new Consumer<PreparedStatement>() {
                        @Override
                        public void accept(PreparedStatement preparedStatement) {
                            try {
                                preparedStatement.setString(1, id);
                                preparedStatement.setString(2, name);
                                preparedStatement.setString(3, finalContent);
                                preparedStatement.setString(4, finalMd5Str);
                                int i = preparedStatement.executeUpdate();
                                System.out.println(i);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            plainCanal = sqlExecutor.queryOne(String.format("select content, content_md5 as md5, status from canal_config where server_id = %s", id), PlainCanal.class);
        }
        if (plainCanal != null) {
            Properties properties = new Properties();
            try {
                properties.load(new ByteArrayInputStream(plainCanal.getContent().getBytes(StandardCharsets.UTF_8)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            plainCanal.setProperties(properties);
        }
        if (StringUtils.isNotBlank(md5) && plainCanal != null && Objects.equals(plainCanal.getMd5(), md5)) {
            return null;
        }
        return plainCanal;
    }

    @Override
    public PlainCanal findInstance(String destination, String md5) {
        PlainCanal plainCanal;

        plainCanal = sqlExecutor.queryOne(String.format("select content, content_md5 as md5, status from canal_instance_config where server_id = %s and name = '%s'", id, destination), PlainCanal.class);
        if (plainCanal != null) {
            Properties properties = new Properties();
            try {
                properties.load(new ByteArrayInputStream(plainCanal.getContent().getBytes(StandardCharsets.UTF_8)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            plainCanal.setProperties(properties);
        }
        if (StringUtils.isNotBlank(md5) && plainCanal != null && Objects.equals(plainCanal.getMd5(), md5)) {
            return null;
        }
        return plainCanal;
    }

    @Override
    public String findInstances(String md5) {
        List<JSONObject> jsonObjects;
        if (StringUtils.isNotBlank(md5)) {
            jsonObjects = sqlExecutor.queryMulti(String.format("select name from canal_instance_config where server_id = %s and content_md5 = '%s'", id, md5), JSONObject.class);
        } else {
            jsonObjects = sqlExecutor.queryMulti(String.format("select name from canal_instance_config where server_id = %s", id), JSONObject.class);
        }
        if (jsonObjects != null) {
            StringJoiner joiner = new StringJoiner(",");
            for (JSONObject jsonObject : jsonObjects) {
                joiner.add(jsonObject.getString("name"));
            }
            return joiner.toString();
        }
        return null;
    }


    @Override
    public boolean handleAlarm(String destination, String msg) {
        return true;
    }
}
