package com.alibaba.otter.canal.admin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.admin.common.TemplateConfigLoader;
import com.alibaba.otter.canal.admin.model.CanalInstanceConfig;
import com.souche.canal.ng.common.Encrypt;
import com.souche.canal.ng.common.Query;
import com.souche.canal.ng.common.account.AccountHelper;
import com.souche.canal.ng.common.account.MysqlAccount;
import com.souche.canal.ng.common.model.CanalConfigDO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * Created on 2021/7/9.
 *
 * @author lan
 * @since 2.0.0
 */
@Component
public class NgSyncService implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(NgSyncService.class);

    @Autowired
    private CanalInstanceService instanceService;

    public void init() {
        Query.init(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://test.database3700.scsite.net:3700/datacenter?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true",
                "souche_rw",
                "hbyA8CPqhr5uZxeXuSeEdm5rhp2ZKF"
        );
        List<CanalConfigDO> configDOList = Query.findList();
        configDOList.stream()
                .filter(Objects::nonNull)
                .forEach(this::sync);
    }

    public boolean sync(CanalConfigDO canalConfigDO) {
        logger.info("start sync, destination: {}, canalConfigDo: {}", canalConfigDO.getName(), JSON.toJSONString(canalConfigDO));
        try {
            CanalInstanceConfig instanceConfig =
                    CanalInstanceConfig.find
                            .query()
                            .where()
                            .eq("name", canalConfigDO.getName())
                            .findOne();
            fromNgToCanal(canalConfigDO, instanceConfig);
        } catch (Exception e) {
            logger.error("sync instance error", e);
            return false;
        }
        logger.info("{} sync success", canalConfigDO.getName());
        return true;
    }

    private void fromNgToCanal(CanalConfigDO canalConfigDO, CanalInstanceConfig instanceConfig) throws IOException {
        Integer clusterId = canalConfigDO.getClusterId();
        String name = canalConfigDO.getName();
        if (instanceConfig == null) {
            logger.info("instanceConfig is not exist, try add one");
            instanceConfig = new CanalInstanceConfig();
            instanceConfig.setClusterServerId("server:" + clusterId);
            instanceConfig.setServerId(Long.valueOf(clusterId));
            instanceConfig.setName(name);
            instanceConfig.setContent(getContent(null, canalConfigDO));
            instanceService.save(instanceConfig);
        } else {
            logger.info("instanceConfig is exist, try update");
            instanceConfig.setClusterServerId("server:" + clusterId);
            instanceConfig.setServerId(Long.valueOf(clusterId));
            instanceConfig.setName(name);
            instanceConfig.setContent(getContent(instanceConfig.getContent(), canalConfigDO));
            instanceService.updateContent(instanceConfig);
        }
        logger.info("fromNgToCanal ClusterServerId: {}, ServerId: {}, Name: {}, content: {}", instanceConfig.getClusterServerId(), instanceConfig.getServerId(), instanceConfig.getName(), instanceConfig.getContent());
    }


    private String getContent(String oldContent, CanalConfigDO canalConfigDO) throws IOException {
        if (StringUtils.isBlank(oldContent)) {
            oldContent = TemplateConfigLoader.loadInstanceConfig();
        }
        Properties content = new Properties();
        content.load(new ByteArrayInputStream(oldContent.getBytes(StandardCharsets.UTF_8)));
        content.setProperty("canal.instance.master.address", getAddress(canalConfigDO));
        String[] mysqlAccountPassword = getMysqlAccountPassword(canalConfigDO);
        content.setProperty("canal.instance.dbUsername", mysqlAccountPassword[0]);
        content.setProperty("canal.instance.dbPassword", mysqlAccountPassword[1]);
        content.setProperty("canal.instance.filter.regex", getRegex(canalConfigDO));
        Set<String> propertiesKeySet = content.stringPropertyNames();

        StringBuilder newContent = new StringBuilder();
        String[] lines = oldContent.split("\n");
        for (String line : lines) {
            findKey:
            {
                for (String key : propertiesKeySet) {
                    if (line.startsWith(key)) {
                        newContent.append(key).append("=").append(content.getProperty(key)).append("\n");
                        break findKey;
                    }
                }
                newContent.append(line).append("\n");
            }
        }
        return newContent.toString();
    }

    private String getRegex(CanalConfigDO canalConfigDO) {
        if (StringUtils.isNotBlank(canalConfigDO.getRegex())) {
            return canalConfigDO.getRegex();
        }
        return ".*\\\\..*";
    }

    String accountUrl = "https://stream-test.dasouche-inc.net/mysql/credentials";

    String tokenUrl = "https://stream-test.dasouche-inc.net/mysql/token";

    String tokenRoleId = "483f0f3c-8d1a-c324-788e-2dd61c47becd";

    String tokenSecretId = "2f2cdf43-31e2-9826-06bb-fb29252345fc";

    private String getAddress(CanalConfigDO canalConfigDO) {
        if (StringUtils.isNotBlank(canalConfigDO.getAddress())) {
            return canalConfigDO.getAddress();
        }
        logger.info("address is blank, using {}:{}", canalConfigDO.getHost(), canalConfigDO.getPort());
        return String.format("%s:%s", canalConfigDO.getHost(), canalConfigDO.getPort());
    }

    private String[] getMysqlAccountPassword(CanalConfigDO canalConfigDO) {
        String[] res;
        if (StringUtils.isNotBlank(canalConfigDO.getPassword())) {
            logger.info("password using canalConfigDO.getPassword()");
            res = new String[]{canalConfigDO.getUsername(), Encrypt.getDecrypt(canalConfigDO.getPassword())};
        } else {
            logger.info("try to get mysql account from config center");
            MysqlAccount account = AccountHelper.getCanalAccount(accountUrl, tokenUrl, tokenRoleId, tokenSecretId, canalConfigDO.getHost(), canalConfigDO.getPort());
            if (account.isSuccess()) {
                logger.info("get password from config center success");
                res = new String[]{account.getUsername(), account.getPassword()};
            } else {
                logger.error("get password from config center failed");
                throw new RuntimeException("get password from config center failed, host&port: " + canalConfigDO.getHost() + ":" + canalConfigDO.getPort());
            }
        }

        return res;
    }

    @Override
    public void run(String... args) throws Exception {
        init();
    }
}
