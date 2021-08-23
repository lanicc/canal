package com.souche.canal.instance.monitor.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.otter.canal.instance.core.CanalInstance;
import com.alibaba.otter.canal.meta.CanalMetaManager;
import com.alibaba.otter.canal.protocol.ClientIdentity;
import com.alibaba.otter.canal.protocol.position.Position;
import com.alibaba.otter.canal.store.CanalEventStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created on 2021/7/12.
 *
 * @author lan
 * @since 2.0.0
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/instance/monitor")
public class MonitorController {

    @GetMapping("/all")
    public Object all() {
        Collection<CanalInstance> canalInstances;
        synchronized (canalInstanceMap) {
            canalInstances = canalInstanceMap.values();
        }

        return canalInstances.stream()
                .map(this::getInstanceRunningState)
                .collect(Collectors.toList());
    }

    @GetMapping("/{destination}")
    public Object destination(@PathVariable("destination") String destination) {
        CanalInstance canalInstance;
        synchronized (canalInstanceMap) {
            canalInstance = canalInstanceMap.get(destination);
        }
        if (canalInstance != null) {
            return getInstanceRunningState(canalInstance);
        }
        return null;
    }

    private InstanceRunningState getInstanceRunningState(CanalInstance canalInstance) {
        String destination = canalInstance.getDestination();
        CanalMetaManager metaManager = canalInstance.getMetaManager();
        List<ClientIdentity> clientIdentityList = metaManager.listAllSubscribeInfo(destination);
        CanalEventStore eventStore = canalInstance.getEventStore();
        Position firstPosition = eventStore.getFirstPosition();
        Position latestPosition = eventStore.getLatestPosition();
        return new InstanceRunningState(destination, clientIdentityList, firstPosition, latestPosition);
    }

    public final Map<String, CanalInstance> canalInstanceMap = new ConcurrentHashMap<>();

    public void register(CanalInstance instance) {
        synchronized (canalInstanceMap) {
            canalInstanceMap.put(instance.getDestination(), instance);
        }
    }

    public void unregister(CanalInstance instance) {
        synchronized (canalInstanceMap) {
            canalInstanceMap.remove(instance.getDestination());
        }
    }

    static class InstanceRunningState {

        private String destination;

        private List<ClientIdentity> clientIdentityList;

        private Position firstPosition;

        private Position latestPosition;

        public InstanceRunningState() {
        }

        public InstanceRunningState(String destination, List<ClientIdentity> clientIdentityList, Position firstPosition, Position latestPosition) {
            this.destination = destination;
            this.clientIdentityList = clientIdentityList;
            this.firstPosition = firstPosition;
            this.latestPosition = latestPosition;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public List<ClientIdentity> getClientIdentityList() {
            return clientIdentityList;
        }

        public void setClientIdentityList(List<ClientIdentity> clientIdentityList) {
            this.clientIdentityList = clientIdentityList;
        }

        public Position getFirstPosition() {
            return firstPosition;
        }

        public void setFirstPosition(Position firstPosition) {
            this.firstPosition = firstPosition;
        }

        public Position getLatestPosition() {
            return latestPosition;
        }

        public void setLatestPosition(Position latestPosition) {
            this.latestPosition = latestPosition;
        }
    }

    @PostConstruct
    public void start() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(() -> {
            Object all = all();
            System.out.println(JSON.toJSONString(all, SerializerFeature.PrettyFormat));
        }, 10, 5, TimeUnit.SECONDS);
    }
}
