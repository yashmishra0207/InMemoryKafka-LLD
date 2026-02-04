package entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@RequiredArgsConstructor
public class ConsumerEntity {
    private final String id;
    private final String consumerGroup;
    private final int maxPollCount;
    private final Map<String, Set<String>> subscribedTopicPartitions = new ConcurrentHashMap<>(); // Map<topicName, Set<partitionIds>>
}
