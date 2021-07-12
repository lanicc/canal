package com.souche.canal.ng.common;

import com.souche.canal.ng.common.model.CanalConfigDO;
import org.junit.Assert;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
public class QueryTest {

    @Before
    public void setUp() throws Exception {
        Query.init(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://test.database3700.scsite.net:3700/datacenter?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true",
                "souche_rw",
                "hbyA8CPqhr5uZxeXuSeEdm5rhp2ZKF"
        );
    }

    @org.junit.Test
    public void findOneByName() {
        CanalConfigDO canalConfigDO = Query.findOneByName("172.17.40.111");
        Assert.assertNotNull(canalConfigDO);
    }
}
