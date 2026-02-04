# In-Memory Message Queue

## Overview

This project implements an **in-memory message queue system**, inspired by systems like **Apache Kafka**.  
It supports topics with multiple partitions, producer/consumer workflows, consumer groups, and provides **at-least-once delivery guarantees**, all while ensuring **thread-safe operations**.

The system is designed for learning, interviews, and prototyping distributed messaging concepts without external dependencies or persistent storage.

---

## Features

### Core Features

- **Topics with Multiple Partitions**
  - Each topic can have one or more partitions.
  - Messages are ordered within a partition.

- **Producers**
  - Producers can publish messages to a topic.
  - Messages are distributed across partitions (e.g., round-robin or key-based).

- **Consumers**
  - Consumers can subscribe to topics.
  - Messages are consumed sequentially per partition.

- **Consumer Groups**
  - Multiple consumers can form a consumer group.
  - Each partition is consumed by only one consumer within a group.
  - Enables parallel consumption while maintaining message order per partition.

- **At-Least-Once Delivery**
  - Messages are guaranteed to be delivered at least once.
  - Consumers may receive duplicate messages in failure scenarios.

- **Thread-Safe**
  - All operations (produce, consume, offset tracking) are safe for concurrent access.

- **In-Memory**
  - All data is stored in memory.
  - No persistence across restarts.

---

## Functional Requirements

- Producer can publish messages to topics
- Consumer can subscribe to topics and consume messages
- Support for multiple partitions per topic
- Messages are stored in memory
- At-least-once delivery guarantee
- Support for consumer groups

This project is intended for educational purposes.
