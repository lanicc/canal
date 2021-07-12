package com.souche.canal.ng.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * canal_config(CanalConfig)表实体类
 *
 * @author lan
 * @since 2021-07-09 16:25:46
 */
@TableName("canal_config")
public class CanalConfigDO {

    @TableId
    private Long id;

    private String name;

    @TableField(value = "clusterId")
    private Integer clusterId;

    private String address;

    @TableField(value = "`key`")
    private String key;

    private String regex;

    /**
     * 域名或者ip
     */
    private String host;

    /**
     * ip列表
     */
    private String ips;

    /**
     * 数据库端口
     */
    private Integer port;

    /**
     * 数据库登录用户名
     */
    private String username;

    /**
     * 数据库登录密码
     */
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
