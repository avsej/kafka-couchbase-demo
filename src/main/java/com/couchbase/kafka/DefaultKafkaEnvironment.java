/**
 * Copyright (C) 2015 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */

package com.couchbase.kafka;

import com.couchbase.client.core.env.DefaultCoreEnvironment;

/**
 * @author Sergey Avseyev
 */
public class DefaultKafkaEnvironment extends DefaultCoreEnvironment implements KafkaEnvironment {
    private static final String KEY_SERIALIZER_CLASS = "kafka.serializer.StringEncoder";
    private static final String VALUE_SERIALIZER_CLASS = "com.couchbase.kafka.JsonEncoder";
    private final String kafkaKeySerializerClass;
    private final String kafkaValueSerializerClass;

    public static DefaultKafkaEnvironment create() {
        return new DefaultKafkaEnvironment(builder());
    }

    public static Builder builder() {
        return new Builder();
    }

    protected DefaultKafkaEnvironment(final Builder builder) {
        super(builder);

        if (!dcpEnabled()) {
            throw new IllegalStateException("Kafka integration cannot work without DCP enabled.");
        }

        kafkaKeySerializerClass = stringPropertyOr("kafka.keySerializerClass", builder.kafkaKeySerializerClass());
        kafkaValueSerializerClass = stringPropertyOr("kafka.valueSerializerClass", builder.kafkaValueSerializerClass());
    }

    @Override
    public String kafkaValueSerializerClass() {
        return kafkaValueSerializerClass;
    }

    @Override
    public String kafkaKeySerializerClass() {
        return kafkaKeySerializerClass;
    }

    public static class Builder extends DefaultCoreEnvironment.Builder implements KafkaEnvironment {
        private String kafkaKeySerializerClass = KEY_SERIALIZER_CLASS;
        private String kafkaValueSerializerClass = VALUE_SERIALIZER_CLASS;

        @Override
        public String kafkaValueSerializerClass() {
            return kafkaValueSerializerClass;
        }

        @Override
        public String kafkaKeySerializerClass() {
            return kafkaKeySerializerClass;
        }

        public void kafkaValueSerializerClass(String className) {
            this.kafkaValueSerializerClass = className;
        }

        public void kafkaKeySerializerClass(String className) {
            this.kafkaKeySerializerClass = className;
        }

        @Override
        public DefaultKafkaEnvironment build() {
            return new DefaultKafkaEnvironment(this);
        }
    }
}
