package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.CanalAdapterConfig;

/**
 * Created on 2021/7/13.
 *
 * @author lan
 * @since 2.0.0
 */
public interface CanalAdapterConfigService {

    void saveOrUpdate(CanalAdapterConfig canalAdapterConfig);

    CanalAdapterConfig detail(Long id);

}
