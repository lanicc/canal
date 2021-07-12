package com.souche.canal.instance.monitor;

import com.alibaba.otter.canal.instance.core.CanalInstance;
import com.alibaba.otter.canal.spi.CanalMetricsService;
import com.souche.canal.instance.monitor.boot.InstanceMonitorApplicationRunner;
import com.souche.canal.instance.monitor.boot.controller.MonitorController;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
public class MetricServiceFacade implements CanalMetricsService {

    private int port;

    private ConfigurableApplicationContext applicationContext;

    private MonitorController monitorController;

    @Override
    public void initialize() {
        applicationContext = InstanceMonitorApplicationRunner.main(new String[]{"--server.port=" + port});
        monitorController = applicationContext.getBean(MonitorController.class);
    }

    @Override
    public void terminate() {
        applicationContext.stop();
    }

    @Override
    public boolean isRunning() {
        return applicationContext.isRunning();
    }

    @Override
    public void register(CanalInstance instance) {
        monitorController.register(instance);
    }

    @Override
    public void unregister(CanalInstance instance) {
        monitorController.unregister(instance);
    }

    @Override
    public void setServerPort(int port) {
        if (port < 1) {
            this.port = 8080;
        } else {
            this.port = port;
        }
    }
}
