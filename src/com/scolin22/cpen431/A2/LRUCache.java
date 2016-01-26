package com.scolin22.cpen431.A2;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by colin on 2016-01-25.
 */
public class LRUCache extends LinkedHashMap<ByteBuffer, Request> {
    private int initialCapacity;

    public LRUCache(int initialCapacity) {
        super(initialCapacity);
        this.initialCapacity = initialCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > this.initialCapacity;
    }
}
