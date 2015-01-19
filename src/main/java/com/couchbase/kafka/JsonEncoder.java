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

import com.couchbase.client.core.message.dcp.DCPRequest;
import com.couchbase.client.core.message.dcp.MutationMessage;
import com.couchbase.client.core.message.dcp.RemoveMessage;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ObjectNode;
import com.couchbase.client.deps.io.netty.util.CharsetUtil;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author Sergey Avseyev
 */
public class JsonEncoder implements Encoder<DCPRequest> {
    private static ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonEncoder.class);

    public JsonEncoder(VerifiableProperties properties) {
    }

    @Override
    public byte[] toBytes(DCPRequest value) {
        try {
            ObjectNode message = MAPPER.createObjectNode();
            if (value instanceof MutationMessage) {
                MutationMessage mutation = (MutationMessage) value;
                message.put("event", "mutation");
                message.put("key", mutation.key());
                message.put("expiration", mutation.expiration());
                message.put("flags", mutation.flags());
                message.put("cas", mutation.cas());
                message.put("lockTime", mutation.lockTime());
                message.set("content", MAPPER.readTree(mutation.content().toString(CharsetUtil.UTF_8)));
            } else if (value instanceof RemoveMessage) {
                RemoveMessage mutation = (RemoveMessage) value;
                message.put("event", "remove");
                message.put("key", mutation.key());
                message.put("cas", mutation.cas());
            }
            return message.toString().getBytes();
        } catch (IOException ex) {
            LOGGER.warn("Error while encoding DCP message", ex);
        }
        return new byte[]{};
    }
}
