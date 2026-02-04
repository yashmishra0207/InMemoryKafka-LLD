package entity;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Partition {
    private final String id;
    private final String topicId;
    private final String name;
    private final List<Message> messages;

    public Partition(final String id, final String topicId) {
        this.id = id;
        this.topicId = topicId;
        this.name = createPartitionName(topicId, id);
        this.messages = new CopyOnWriteArrayList<>();
    }

    private String createPartitionName(final String topicId, final String partitionId) {
        return String.format("%s_%s", topicId, partitionId);
    }
}
