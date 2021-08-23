package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.common.TemplateConfigLoader;
import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.CanalAdapterConfig;
import com.alibaba.otter.canal.admin.service.CanalAdapterConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2021/7/13.
 *
 * @author lan
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api/{env}/canal")
public class CanalAdapterConfigController {

    @Autowired
    private CanalAdapterConfigService canalAdapterConfigService;

    @GetMapping("/adapter/{id}")
    public BaseModel<CanalAdapterConfig> detail(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(canalAdapterConfigService.detail(id));
    }

    @PutMapping("/adapter")
    public BaseModel<String> update(@RequestBody CanalAdapterConfig canalAdapterConfig, @PathVariable String env) {
        canalAdapterConfigService.saveOrUpdate(canalAdapterConfig);
        return BaseModel.getInstance("success");
    }

    @GetMapping("/adapter/template")
    public BaseModel<String> template(@PathVariable String env) {
        return BaseModel.getInstance(TemplateConfigLoader.loadAdapterConfig());
    }

    private String s = "groups:\n  - groupId: g1\n    outerAdapters:\n      - name: logger\n  - groupId: g2\n    outerAdapters:\n      - name: logger";
}
