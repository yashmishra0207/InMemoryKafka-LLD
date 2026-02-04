package service;

public class KafkaService {

    public static String createTopic(final String topicName, final int partitionCount) {
        return TopicService.createTopic(topicName, partitionCount);
    }
}
