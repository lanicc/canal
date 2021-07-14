package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.model.CanalAdapterConfig;
import com.alibaba.otter.canal.admin.service.CanalAdapterConfigService;
import org.springframework.stereotype.Service;

/**
 * Created on 2021/7/13.
 *
 * @author lan
 * @since 2.0.0
 */
@Service
public class CanalAdapterConfigServiceImpl implements CanalAdapterConfigService {
    @Override
    public void saveOrUpdate(CanalAdapterConfig canalAdapterConfig) {
        canalAdapterConfig.delete();
        canalAdapterConfig.insert();
    }

    @Override
    public CanalAdapterConfig detail(Long id) {
        return CanalAdapterConfig.find.byId(id);
    }
}
