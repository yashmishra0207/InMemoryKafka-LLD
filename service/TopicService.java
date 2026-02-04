package service;

import entity.Message;
import entity.Topic;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TopicService {

    private static final Map<String, Topic> topics = new ConcurrentHashMap<>(); // TopicName to Topic, topicName is unique

    public static String createTopic(final String topicName, final int partitionCount) {

        if (!Objects.isNull(topics.get(topicName))) {
            System.out.printf("Topic: %s already exists%n", topicName);
            return topicName;
        }

        final var newTopicId = UUID.randomUUID().toString();
        final var partitions = new CopyOnWriteArrayList<String>();

        for (int i = 0; i < partitionCount; i++) {
            final var partitionId = PartitionService.createPartition(newTopicId);
            partitions.add(partitionId);
        }

        final var newTopic = new Topic(newTopicId, topicName, partitions);
        topics.put(newTopic.getName(), newTopic);

        return newTopic.getName();
    }

    public static boolean pushMessage(final String topicName, final Message message) {

        final var topic = topics.get(topicName);
        if (Objects.isNull(topic)) {
            System.out.printf("Topic: %s not found", topicName);
            return false;
        }

        final var topicPartitions = topic.getPartitions();
        final var partitionIdx = Math.abs(message.key().hashCode()) % topicPartitions.size();

        final var partitionId = topicPartitions.get(partitionIdx);

        PartitionService.pushMessage(partitionId, message);

        return true;
    }

    public static Topic getTopic(final String topicName) {

        return topics.get(topicName);
    }
}
