package service;

import entity.Message;
import entity.Partition;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PartitionService {
    private static final Map<String, Partition> partitions = new ConcurrentHashMap<>(); // partitionId to partition

    public static String createPartition(final String topicId) {
        final var newPartitionId = UUID.randomUUID().toString();
        final var newPartition = new Partition(newPartitionId, topicId);

        partitions.put(newPartitionId, newPartition);

        return newPartitionId;
    }

    public static boolean pushMessage(final String partitionId, final Message message) {
        final var partition = partitions.get(partitionId);
        if (Objects.isNull(partition)) {
            System.out.printf("Partition with id: %s not found%n", partitionId);
            return false;
        }

        partition.getMessages().add(message);

        return true;
    }

    public static Partition getPartition(final String partitionId) {
        return partitions.get(partitionId);
    }
}
