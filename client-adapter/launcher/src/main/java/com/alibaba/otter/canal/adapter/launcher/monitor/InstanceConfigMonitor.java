package com.alibaba.otter.canal.adapter.launcher.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.adapter.launcher.loader.CanalAdapterService;
import com.alibaba.otter.canal.adapter.launcher.monitor.remote.ConfigItem;
import com.alibaba.otter.canal.adapter.launcher.monitor.rmt.DbRmtConfigLoader;
import com.alibaba.otter.canal.adapter.launcher.monitor.rmt.RmtConfigLoaderFactory;
import com.alibaba.otter.canal.client.adapter.support.CanalClientConfig;
import com.alibaba.otter.canal.common.utils.NamedThreadFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
@Component
public class InstanceConfigMonitor implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InstanceConfigMonitor.class);

    final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, new NamedThreadFactory("remote-instance-adapter-config-scan"));

    @Autowired
    private Environment env;

    @Autowired
    private CanalAdapterService canalAdapterService;

    private DbRmtConfigLoader remoteConfigLoader;

    private final Map<Long, ConfigItem> adapterConfigs = new ConcurrentHashMap<>();


    private void monitor() {
        List<ConfigItem> configItemList = remoteConfigLoader.loadRemoteConfig();
        Iterator<ConfigItem> iterator = configItemList.iterator();
        HashMap<Long, ConfigItem> configItemMap = new HashMap<>(adapterConfigs);
        while (iterator.hasNext()) {
            ConfigItem newConfigItem = iterator.next();
            ConfigItem oldConfigItem = configItemMap.remove(newConfigItem.getId());
            if (oldConfigItem == null) {
                add(newConfigItem);
            } else {
                if (!Objects.equals(newConfigItem.getModifiedTime(), oldConfigItem.getModifiedTime())) {
                    update(newConfigItem);
                }
            }
        }
        if (!configItemMap.isEmpty()) {
            for (Long id : configItemMap.keySet()) {
                remove(id);
            }
        }
    }

    private void add(ConfigItem configItem) {
        adapterConfigs.put(configItem.getId(), configItem);
        canalAdapterService.add(toAdapter(configItem));
    }

    private void update(ConfigItem configItem) {
        remove(configItem.getId());
        add(configItem);
    }

    private void remove(long id) {
        ConfigItem configItem = adapterConfigs.remove(id);
        canalAdapterService.remove(toAdapter(configItem));
    }

    private CanalClientConfig.CanalAdapter toAdapter(ConfigItem configItem) {
        String content = configItem.getContent();
        if (StringUtils.isNotBlank(content)) {
            Object loaded = new Yaml().load(content);
            String loadedJson = JSON.toJSONString(loaded);
            CanalClientConfig.CanalAdapter canalAdapter = JSON.parseObject(loadedJson, CanalClientConfig.CanalAdapter.class);
            canalAdapter.setInstance(configItem.getName());
            return canalAdapter;
        }
        return null;
    }

    @Override
    public void run(String... args) {
        remoteConfigLoader = RmtConfigLoaderFactory.getRemoteConfigLoader(env);
        if (remoteConfigLoader != null) {
            executor.scheduleWithFixedDelay(() -> {
                try {
                    monitor();
                } catch (Exception e) {
                    logger.error("scan remote instance adapter configs failed", e);
                }
            }, 0, 3, TimeUnit.SECONDS);
        }
    }
}
