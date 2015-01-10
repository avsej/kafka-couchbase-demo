package com.couchbase.kafka;

import com.couchbase.client.core.env.CoreEnvironment;

/**
 * @author Sergey Avseyev
 */
public interface KafkaEnvironment extends CoreEnvironment {
    String kafkaValueSerializerClass();
    String kafkaKeySerializerClass();
}
