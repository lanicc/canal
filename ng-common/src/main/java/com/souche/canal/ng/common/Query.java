package com.souche.canal.ng.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.souche.canal.ng.common.dao.CanalConfigDao;
import com.souche.canal.ng.common.model.CanalConfigDO;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Function;

/**
 * Created on 2021/7/9.
 *
 * @author lan
 * @since 2.0.0
 */
public final class Query {

    public static List<CanalConfigDO> findListByClusterId(Integer clusterId) {
        return doDb(canalConfigDao -> canalConfigDao.selectList(Wrappers.<CanalConfigDO>query().eq("clusterId", clusterId)), CanalConfigDao.class);
    }

    public static List<CanalConfigDO> findList() {
        return doDb(canalConfigDao -> canalConfigDao.selectList(Wrappers.emptyWrapper()), CanalConfigDao.class);
    }

    public static CanalConfigDO findOneByName(String name) {
        return doDb(canalConfigDao -> canalConfigDao.selectOne(Wrappers.<CanalConfigDO>query().eq("name", name)), CanalConfigDao.class);
    }

    private static <T, E> E doDb(Function<T, E> dbDoFunc, Class<T> clazz) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return dbDoFunc.apply(sqlSession.getMapper(clazz));
        } finally {
            sqlSession.close();
        }
    }

    private static volatile SqlSessionFactory sqlSessionFactory;

    public static void init(String driverClassName, String url, String username, String password) {
        if (sqlSessionFactory == null) {
            synchronized (Query.class) {
                if (sqlSessionFactory != null) {
                    return;
                }
                DataSource dataSource;
                try {
                    dataSource = dataSource(driverClassName, url, username, password);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                TransactionFactory transactionFactory = new JdbcTransactionFactory();
                Environment environment = new Environment("Production", transactionFactory, dataSource);
                MybatisConfiguration configuration = new MybatisConfiguration(environment);
                configuration.addMapper(CanalConfigDao.class);
                configuration.setLogImpl(StdOutImpl.class);
                sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(configuration);
            }
        }

    }


    private static DataSource dataSource(String driverClassName, String url, String username, String password) throws ClassNotFoundException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

}
