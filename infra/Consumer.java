package infra;

import entity.ConsumerEntity;
import entity.Message;
import service.ConsumerService;
import service.OffsetTrackingService;
import service.PartitionService;
import service.TopicService;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Consumer extends ConsumerEntity {

    public Consumer(final String consumerGroup, final int maxPollCount) {
        final var consumerId = UUID.randomUUID().toString();
        super(consumerId, consumerGroup, maxPollCount);
        ConsumerService.registerConsumer(this);
    }

    public boolean subscribe(List<String> topicNames) {
        final var consumer = ConsumerService.getConsumer(this.getId());
        if (Objects.isNull(consumer)) {
            System.out.printf("Consumer: %s not found%n", this.getId());
            return false;
        }

        // perform rebalancing between consumers and partitions by each topic and its various consumer groups
        for (String topicName: topicNames) {

            // topic already subscribed, no change in consumer count for that topic and hence no rebalancing required
            if (!Objects.isNull(consumer.getSubscribedTopicPartitions().get(topicName))) {
                System.out.printf("Topic: %s already subscribed by ConsumerId: %s%n", topicName, consumer.getId());
                continue;
            }

            final var topic = TopicService.getTopic(topicName);
            if (Objects.isNull(topic)) {
                System.out.printf("Topic: %s not found%n", topicName);
                continue;
            }

            topic.getConsumers().add(consumer.getId());

            final var consumersInNewConsumersConsumerGroup = topic.getConsumers().stream()
                    .map(ConsumerService::getConsumer)
                    .filter(c -> Objects.equals(c.getConsumerGroup(), consumer.getConsumerGroup()))
                    .toList();

            final var partitionIds = topic.getPartitions();

            final var currentConsumerGroupSize = consumersInNewConsumersConsumerGroup.size();

            // new consumer will sit idle as there are more consumers than partitions
            if (currentConsumerGroupSize > partitionIds.size()) {
                System.out.println("More consumers than partitions hence no rebalancing");
                continue;
            }

            int base = partitionIds.size() / currentConsumerGroupSize;
            int remainder = partitionIds.size() % currentConsumerGroupSize;
            int assignedPartitionCount = 0;

            for (int i = 0; i < currentConsumerGroupSize; i++) {

                int partitionsForConsumer = i < remainder ? base + 1 : base;

                final var assignedPartitionIds = new HashSet<String>();
                for (int j = assignedPartitionCount; j < assignedPartitionCount + partitionsForConsumer; j++) {
                    assignedPartitionIds.add(partitionIds.get(j));
                }

                consumer.getSubscribedTopicPartitions().put(topicName, assignedPartitionIds);
                assignedPartitionCount += partitionsForConsumer;
            }

        }

        return true;
    }

    // Note: considering auto-acknowledgement
    public Map<String, Map<String, List<Message>>> poll() { // Map<TopicName, Map<PartitionId, List<Message>>>
        final var records = new HashMap<String, Map<String, List<Message>>>();

        // TODO: move commited offset pointer if the message is read by consumers in all the consumerGroups
        final var subscribedTopicPartitions = this.getSubscribedTopicPartitions();
        final var currentConsumerGroup = this.getConsumerGroup();

        for (var topicPartitions: subscribedTopicPartitions.entrySet()) {
            final var partitionMessagesMap = new HashMap<String, List<Message>>();

            for (var partitionId: topicPartitions.getValue()) {
                var unreadMessageStartIndex = OffsetTrackingService.getTopicPartitionConsumerGroupMessageOffset(
                        topicPartitions.getKey(), partitionId, currentConsumerGroup);

                final var partitionMessages = PartitionService.getPartition(partitionId).getMessages();

                final var polledMessages = new CopyOnWriteArrayList<Message>();
                int i = unreadMessageStartIndex;
                while (i < partitionMessages.size() && polledMessages.size() < this.getMaxPollCount()) {
                    final var message = partitionMessages.get(i);
                    polledMessages.add(message);
                    i++;
                }

                OffsetTrackingService.setTopicPartitionConsumerGroupMessageOffset(
                        topicPartitions.getKey(), partitionId, currentConsumerGroup, i
                );

                partitionMessagesMap.put(partitionId, polledMessages);
            }

            records.put(topicPartitions.getKey(), partitionMessagesMap);
        }

        return records;
    }
}
