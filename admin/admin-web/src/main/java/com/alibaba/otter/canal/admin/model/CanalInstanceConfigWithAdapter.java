package com.alibaba.otter.canal.admin.model;

/**
 * Created on 2021/8/23.
 *
 * @author lan
 * @since 2.0.0
 */
public class CanalInstanceConfigWithAdapter extends CanalInstanceConfig {

    private String adapterStatus;

    public String getAdapterStatus() {
        return adapterStatus;
    }

    public void setAdapterStatus(String adapterStatus) {
        this.adapterStatus = adapterStatus;
    }
}
