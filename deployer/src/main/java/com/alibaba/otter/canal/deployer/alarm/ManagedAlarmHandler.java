package com.alibaba.otter.canal.deployer.alarm;

import com.alibaba.otter.canal.common.AbstractCanalLifeCycle;
import com.alibaba.otter.canal.common.alarm.CanalAlarmHandler;
import com.alibaba.otter.canal.instance.manager.plain.PlainCanalConfigClient;
import com.alibaba.otter.canal.instance.manager.plain.PlainCanalConfigClientFactory;

/**
 * Created on 2021/7/10.
 *
 * @author lan
 * @since 2.0.0
 */
public class ManagedAlarmHandler extends AbstractCanalLifeCycle implements CanalAlarmHandler {

//    private String managerAddress;
//
//    private String adminUser;
//
//    private String adminPasswd;
//
//    private String registerIp;
//
//    private String adminPort;

    private static volatile PlainCanalConfigClient configClient;

    public ManagedAlarmHandler() {
        initPlainCanalConfigClient();
    }

    @Override
    public void sendAlarm(String destination, String msg) {
        boolean restart = configClient.handleAlarm(destination, msg);
        if (restart) {
            System.out.println("restart");
        }

    }

    private static void initPlainCanalConfigClient() {
        if (configClient == null) {
            configClient = PlainCanalConfigClientFactory.getCanalConfigClient();
        }
    }

}
