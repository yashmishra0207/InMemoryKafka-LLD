import entity.Message;
import infra.Consumer;
import infra.Producer;
import service.KafkaService;

import java.util.List;
import java.util.Map;

class Main {

    public static void main() {
        KafkaService.createTopic("orders", 5);

        final var producer = new Producer("p1");

        final var consumer = new Consumer("cg1", 3);
        consumer.subscribe(List.of("orders"));

        producer.send("orders", new Message("order-1", "order 1 message 1"));
        producer.send("orders", new Message("order-2", "order 2 message 1"));
        producer.send("orders", new Message("order-1", "order 1 message 2"));
        producer.send("orders", new Message("order-4", "order 4 message 1"));
        producer.send("orders", new Message("order-3", "order 3 message 1"));
        producer.send("orders", new Message("order-1", "order 1 message 3"));
        producer.send("orders", new Message("order-1", "order 1 message 4"));

        var messages = consumer.poll();
        printMessages(messages);

        messages = consumer.poll();
        printMessages(messages);
    }

    private static void printMessages(final Map<String, Map<String, List<Message>>> messages) {
        messages.forEach((topicName, value) ->
                value.forEach((partitionKey, messageList) ->
                        System.out.printf("%s -> %s -> %s%n", topicName, partitionKey, messageList)
                )
        );

        System.out.println("---------------------------------------------------------------");
    }
}
