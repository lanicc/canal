package com.alibaba.otter.canal.deployer.alarm;

import com.alibaba.otter.canal.common.AbstractCanalLifeCycle;
import com.alibaba.otter.canal.common.alarm.CanalAlarmHandler;
import com.alibaba.otter.canal.instance.manager.plain.PlainCanalConfigClient;

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

    public ManagedAlarmHandler(String canalId, String managerAddress, String adminUser, String adminPasswd, String registerIp, int adminPort) {
//        this.managerAddress = managerAddress;
//        this.adminUser = adminUser;
//        this.adminPasswd = adminPasswd;
//        this.registerIp = registerIp;
//        this.adminPort = adminPort;
        initPlainCanalConfigClient(managerAddress, adminUser, adminPasswd, registerIp, adminPort);
    }

    @Override
    public void sendAlarm(String destination, String msg) {
        boolean restart = configClient.handleAlarm(destination, msg);
        if (restart) {
            System.out.println("restart");
        }

    }

    private static void initPlainCanalConfigClient(String managerAddress, String adminUser, String adminPasswd, String registerIp, int adminPort) {
        if (configClient == null) {
            synchronized (ManagedAlarmHandler.class) {
                if (configClient == null) {
                    configClient = new PlainCanalConfigClient(managerAddress, adminUser, adminPasswd, registerIp, adminPort);
                }
            }
        }
    }

}
