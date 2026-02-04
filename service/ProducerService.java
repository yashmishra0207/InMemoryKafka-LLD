package service;

import infra.Producer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProducerService {

    private static final Map<String, Producer> producers = new ConcurrentHashMap<>(); // producerId to producer

    public static void registerProducer(final Producer producer) {
        producers.put(producer.getId(), producer);
    }
}
