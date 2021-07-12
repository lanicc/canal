package com.souche.canal.instance.monitor;

import com.alibaba.otter.canal.spi.CanalMetricsProvider;
import com.alibaba.otter.canal.spi.CanalMetricsService;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
public class InstanceMonitorProvider implements CanalMetricsProvider {
    @Override
    public CanalMetricsService getService() {
        return new MetricServiceFacade();
    }
}
