package com.alibaba.otter.canal.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.CanalInstanceConfig;
import com.alibaba.otter.canal.admin.service.CanalInstanceService;
import com.alibaba.otter.canal.admin.service.NgSyncService;
import com.souche.canal.ng.common.Query;
import com.souche.canal.ng.common.model.CanalConfigDO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created on 2021/7/10.
 *
 * @author lan
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api/{env}/alarm")
public class AlarmController {

    Logger logger = LoggerFactory.getLogger(AlarmController.class);

    @Autowired
    private NgSyncService ngSyncService;

    @Autowired
    private CanalInstanceService canalInstanceService;

    @PostMapping("/handle")
    public BaseModel<Boolean> handle(@RequestBody Map<String, String> data) {
        logger.error("received alarm: {}", JSON.toJSONString(data));
        String destination = data.get("destination");
        String msg = data.get("msg");
        if (StringUtils.isNotBlank(destination) && StringUtils.isNotBlank(msg)) {
            if (msg.contains("Access")) {
                CanalConfigDO configDO = Query.findOneByName(destination);
                if (configDO != null) {
                    configDO.setPassword(null);
                    boolean sync = ngSyncService.sync(configDO);
                    if (sync) {
                        return BaseModel.getInstance(Boolean.TRUE);
                    }
                }
            } else if (msg.contains("SocketTimeoutException")
                    || msg.contains("SocketException")
                    || msg.contains("Connection refused")
                    || msg.contains("UnknownHostException")) {
                CanalInstanceConfig canalInstanceConfig = CanalInstanceConfig.find.query()
                        .where()
                        .eq("name", destination)
                        .findOne();
                if (canalInstanceConfig != null) {
                    logger.info("try to stop {}", destination);
                    boolean stop = canalInstanceService.remoteOperation(canalInstanceConfig.getId(), canalInstanceConfig.getServerId(), "stop");
                    logger.info("stop result: {}", stop);
                }
            }
        }
        return BaseModel.getInstance(Boolean.FALSE);
    }
}
