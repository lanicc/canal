package com.souche.baby.search.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.adapter.OuterAdapter;
import com.alibaba.otter.canal.client.adapter.support.Dml;
import com.alibaba.otter.canal.client.adapter.support.OuterAdapterConfig;
import com.alibaba.otter.canal.client.adapter.support.SPI;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created on 2021/8/12.
 *
 * @author lan
 * @since 2.0.0
 */
@SPI("baby-search")
public class BabySearchAdapter implements OuterAdapter {

    Producer<String, String> producer;

    @Override
    public void init(OuterAdapterConfig configuration, Properties envProperties) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    @Override
    public void sync(List<Dml> dmls) {
        for (Dml dml : dmls) {
            List<Map<String, Object>> olds = dml.getOld();
            Iterator<Map<String, Object>> iterator = olds.iterator();
            List<Map<String, Object>> datas = dml.getData();
            for (Map<String, Object> data : datas) {
                HashMap<Object, Object> map = new HashMap<>();
                map.put("data", data);
                map.put("database", dml.getDatabase());
                map.put("table", dml.getTable());
                map.put("instanceName", dml.getDestination());
                map.put("destination", dml.getDestination());
                map.put("old", iterator.next());
                map.put("type", dml.getType());
                Future<RecordMetadata> future = producer.send(new ProducerRecord<>("test", null, JSON.toJSONString(map)));
                try {
                    RecordMetadata metadata = future.get();
                    System.out.println(metadata.offset());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void destroy() {

    }

}
