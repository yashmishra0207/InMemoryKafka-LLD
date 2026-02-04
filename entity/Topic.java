package entity;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Topic {
    private final String id;
    private final String name;
    private final List<String> partitions;
    private final Set<String> consumers = new HashSet<>();

    public Topic(final String id,
                 final String topicName,
                 final List<String> partitions) {
        this.id = id;
        this.name = topicName;
        this.partitions = partitions;
    }
}
