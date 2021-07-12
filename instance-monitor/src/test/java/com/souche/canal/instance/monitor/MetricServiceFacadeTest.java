package com.souche.canal.instance.monitor;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
public class MetricServiceFacadeTest {

    @org.junit.Test
    public void initialize() throws InterruptedException {
        MetricServiceFacade metricServiceFacade = new MetricServiceFacade();
        metricServiceFacade.setServerPort(8000);
        metricServiceFacade.initialize();
        MetricServiceFacade metricServiceFacade1 = new MetricServiceFacade();
        metricServiceFacade1.setServerPort(8080);
        metricServiceFacade1.initialize();
    }
}
