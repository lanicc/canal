package com.souche.canal.instance.monitor.boot;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
@SpringBootApplication
public class InstanceMonitorApplicationRunner {

    public static ConfigurableApplicationContext main(String[] args) {
        SpringApplication application = new SpringApplication(InstanceMonitorApplicationRunner.class);
        application.setBannerMode(Banner.Mode.OFF);
        return application.run(args);
    }
}
