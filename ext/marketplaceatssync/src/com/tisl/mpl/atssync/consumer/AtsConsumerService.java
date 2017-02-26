package com.tisl.mpl.atssync.consumer;

import com.tisl.mpl.marketplacecommerceservices.service.ExtStockService;
import de.hybris.platform.ordersplitting.impl.DefaultWarehouseService;
import de.hybris.platform.servicelayer.model.ModelService;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by i325571 on 08/02/17.
 */
public class AtsConsumerService {

    private static final Logger LOG = Logger.getLogger(AtsConsumerService.class);
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    @Value("${marketplaceatssync.ats.consumer.threadcount}")
    private int THREAD_COUNT;

    @Value("${marketplaceatssync.ats.consumer.enabled}")
    private boolean enableJob;

    @Resource(name = "deserializer")
    private AvailabilityToSellDTODeserializer availabilityToSellDTODeserializer;

    @Resource(name = "modelService")
    private ModelService modelService;

    @Resource(name = "stockService")
    private ExtStockService stockService;

    @Resource(name = "defaultWarehouseService")
    private DefaultWarehouseService defaultWarehouseService;

    private static final String GROUP_ID = "group.id";
    private static final String ENABLE_AUTO_COMMIT = "enable.auto.commit";
    private static final String AUTO_COMMIT_INTERVAL_MS = "auto.commit.interval.ms";
    private static final String SESSION_TIMEOUT_MS = "session.timeout.ms";
    private static final String ZOOKEEPER_TIMEOUT_MS = "zookeeper.sync.time.ms";
    private static final String ZOOKEEPER_CONNECT = "zookeeper.connect";


    public AtsConsumerService(String a_zookeeper, String a_groupId, String a_topic, boolean autoCommit,
                              String sessionTimeout,String commitInterval,String zookeeperTimeout) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId, autoCommit, sessionTimeout, commitInterval, zookeeperTimeout));
        this.topic = a_topic;
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (executor != null && !executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                LOG.info("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            LOG.error("Interrupted during shutdown, exiting uncleanly" , e);
        }
    }

    private void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, a_numThreads);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        LOG.info("Stream List Size is : "+streams.size());
        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);
        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            executor.submit(new AtsConsumer(stream, threadNumber, availabilityToSellDTODeserializer,
                    modelService,stockService,defaultWarehouseService));
            threadNumber++;
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId, boolean autocommit,
                                                       String sessionTimeout,String commitInterval,String zookeeperTimeout) {
        final Properties props = new Properties();
        props.put(ZOOKEEPER_CONNECT, a_zookeeper);
        props.put(GROUP_ID, a_groupId);
        props.put(ENABLE_AUTO_COMMIT, autocommit);
        props.put(SESSION_TIMEOUT_MS, sessionTimeout);
        props.put(ZOOKEEPER_TIMEOUT_MS, zookeeperTimeout);
        props.put(AUTO_COMMIT_INTERVAL_MS, commitInterval);

        return new ConsumerConfig(props);
    }

    public void call() {
        if(enableJob) {
            this.run(THREAD_COUNT);
        }
    }
}
