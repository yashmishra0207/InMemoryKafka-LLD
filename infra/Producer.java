package infra;

import entity.Message;
import entity.ProducerEntity;
import service.ProducerService;
import service.TopicService;

import java.util.UUID;

public class Producer extends ProducerEntity {

    public Producer(String name) {
        final var producerId = UUID.randomUUID().toString();
        super(producerId, name);
        ProducerService.registerProducer(this);
    }

    public boolean send(final String topicName, final Message message) {
        return TopicService.pushMessage(topicName, message);
    }
}
