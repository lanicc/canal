package com.alibaba.otter.canal.adapter.launcher.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.adapter.support.CanalClientConfig;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created on 2021/7/13.
 *
 * @author lan
 * @since 2.0.0
 */
public class InstanceConfigMonitorTest {

    @Test
    public void yamlToGroup() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("instance-example.yml");
        Object loaded = new Yaml().load(classPathResource.getInputStream());
        String loadedJson = JSON.toJSONString(loaded);
        CanalClientConfig.CanalAdapter canalAdapter = JSON.parseObject(loadedJson, CanalClientConfig.CanalAdapter.class);
        System.out.println(canalAdapter);
    }

}
