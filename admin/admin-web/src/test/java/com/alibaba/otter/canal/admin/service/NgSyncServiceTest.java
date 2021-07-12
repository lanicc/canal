package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.common.TemplateConfigLoader;
import com.google.common.io.LineReader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
public class NgSyncServiceTest {

    @Test
    public void sync() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("canal.mq.partition", "1");
        Set<String> propertiesKeySet = properties.stringPropertyNames();
        String s = TemplateConfigLoader.loadInstanceConfig();

        StringBuilder content = new StringBuilder();
        String[] lines = s.split("\n");
        for (String line : lines) {
            findKey: {
                for (String key : propertiesKeySet) {
                    if (line.startsWith(key)) {
                        content.append(key).append("=").append(properties.getProperty(key)).append("\n");
                        break findKey;
                    }
                }
                content.append(line).append("\n");
            }
        }

        System.out.println(content);
    }




}
