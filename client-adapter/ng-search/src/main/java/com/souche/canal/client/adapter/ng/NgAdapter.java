package com.souche.canal.client.adapter.ng;

import com.alibaba.otter.canal.client.adapter.OuterAdapter;
import com.alibaba.otter.canal.client.adapter.support.Dml;
import com.alibaba.otter.canal.client.adapter.support.OuterAdapterConfig;
import com.alibaba.otter.canal.client.adapter.support.SPI;
import com.souche.canal.ng.common.Query;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created on 2021/7/9.
 *
 * @author lan
 * @since 2.0.0
 */
@SPI("ng")
public class NgAdapter implements OuterAdapter {
    @Override
    public void init(OuterAdapterConfig configuration, Properties envProperties) {
        initQuery(configuration);
    }

    @Override
    public void sync(List<Dml> dmls) {
        System.out.println(dmls);
    }

    @Override
    public void destroy() {

    }

    private void initQuery(OuterAdapterConfig configuration) {
        Map<String, String> properties = configuration.getProperties();
        String driverClassName = properties.get("jdbc.driverClassName");
        String url = properties.get("jdbc.url");
        String username = properties.get("jdbc.username");
        String password = properties.get("jdbc.password");
        Query.init(driverClassName, url, username, password);
    }
}
