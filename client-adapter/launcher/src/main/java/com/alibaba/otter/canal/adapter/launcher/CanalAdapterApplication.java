package com.alibaba.otter.canal.adapter.launcher;

import com.souche.canal.client.adapter.ng.NgAdapter;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动入口
 *
 * @author rewerma @ 2018-10-20
 * @version 1.0.0
 */
@SpringBootApplication
public class CanalAdapterApplication {

    public static void main(String[] args) {
        System.out.println(NgAdapter.class);
        SpringApplication application = new SpringApplication(CanalAdapterApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
