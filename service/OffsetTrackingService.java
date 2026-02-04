package service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OffsetTrackingService {

    // Map<topicName, Map<partitionId, Map<consumerGroupName, polledTillIndex>>>
    private static final Map<String, Map<String, Map<String, Integer>>> topicPartitionConsumerGroupMessageOffset =
            new ConcurrentHashMap<>();

    public static void setTopicPartitionConsumerGroupMessageOffset(final String topicName,
                                                                   final String partitionId,
                                                                   final String consumerGroupName,
                                                                   final Integer polledTillIndex) {

        topicPartitionConsumerGroupMessageOffset
                .computeIfAbsent(topicName, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(partitionId, k -> new ConcurrentHashMap<>())
                .put(consumerGroupName, polledTillIndex);
    }

    public static Integer getTopicPartitionConsumerGroupMessageOffset(final String topicName,
                                                                      final String partitionId,
                                                                      final String consumerGroupName) {

        return topicPartitionConsumerGroupMessageOffset.getOrDefault(topicName, new HashMap<>())
                .getOrDefault(partitionId, new HashMap<>())
                .getOrDefault(consumerGroupName, 0);
    }
}
