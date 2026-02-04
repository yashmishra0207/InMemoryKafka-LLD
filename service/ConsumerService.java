package service;

import infra.Consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerService {
    private static final Map<String, Consumer> consumers = new ConcurrentHashMap<>(); // producerId to producer

    public static void registerConsumer(final Consumer consumer) {
        consumers.put(consumer.getId(), consumer);
    }

    public static Consumer getConsumer(final String consumerId) {
        return consumers.get(consumerId);
    }
}
