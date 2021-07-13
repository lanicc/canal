### 弃用的开源的功能

- BootstrapConfiguration：删除了自动配置（META-INF/spring.factories）
- ApplicationConfigMonitor:
  不注册到Spring容器，该类是用来扫描配置文件的变更的（配合com.alibaba.otter.canal.adapter.launcher.monitor.remote包可以实现从数据库动态加载配置）
- CanalAdapterService: 删除@RefreshScope注解，不需要这个功能

### 新增的功能

- CanalAdapterLoader: 增加CanalClientConfig.CanalAdapter维度的动态监听和暂停，CanalAdapterService也增加了相应的接口
- InstanceConfigMonitor: 增加了基于canalInstance维度的配置监听，配合com.alibaba.otter.canal.adapter.launcher.monitor.rmt包，完成监听数据库配置


### 基于数据库的动态配置监听

1. 查询配置：sql 
   ````sql
   select cac.id, cic.name, cac.content, cac.modified_time
   from canal_adapter_config cac
   inner join canal_instance_config cic on cac.id = cic.id
   ````
2. 对比每一项的modified_time是否有更新，有更新的认为配置更改了
3. 根据1中的sql，可知CanalClientConfig.CanalAdapter中的name，是canal_instance_config的name

### canal_adapter_config表使用说明

- id: canal_instance_config的id
- category: 目前没用
- name: 目前没用
- status: 0表示正常启用，其他表示不启用
- content: yaml格式的配置，示例如下：
  ````yaml
  groups:
    - groupId: g1
      outerAdapters:
        - name: logger
    - groupId: g2
      outerAdapters:
        - name: logger
  ````
- modified_time: 扫描数据库时，会根据modified_time判断配置是否修改
