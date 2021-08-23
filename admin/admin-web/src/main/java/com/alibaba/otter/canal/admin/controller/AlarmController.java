package com.alibaba.otter.canal.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.service.CanalInstanceService;
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
    private CanalInstanceService canalInstanceService;

    @PostMapping("/handle")
    public BaseModel<Boolean> handle(@RequestBody Map<String, String> data) {
        logger.error("received alarm: {}", JSON.toJSONString(data));

        return BaseModel.getInstance(Boolean.FALSE);
    }
}
