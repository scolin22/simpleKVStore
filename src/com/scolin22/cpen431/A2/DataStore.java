package com.scolin22.cpen431.A2;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    final static int CAPACITY = 100000;
    ConcurrentHashMap<ByteBuffer, byte[]> ds;

    public DataStore() {
        this.ds = new ConcurrentHashMap<ByteBuffer, byte[]>();
    }

    public void put(Request r) {
        if (ds.size() >= CAPACITY) {
            r.repType = Request.ReplyType.NO_SPACE;
            return;
        }
        if (r.length < 0 || r.length > Request.MAX_VAL_LENGTH) {
            r.repType = Request.ReplyType.BAD_VAL_LEN;
            return;
        }
        ds.put(r.getKey(), r.value);
        r.repType = Request.ReplyType.OP_SUCCESS;
    }

    public void get(Request r) {
        if (!ds.containsKey(r.getKey())) {
            r.repType = Request.ReplyType.INVALID_KEY;
            return;
        }
        byte[] store = ds.get(r.getKey());
        r.length = (short) store.length;
        r.value = store;
        r.repType = Request.ReplyType.OP_SUCCESS;
    }

    public void remove(Request r) {
        if (!ds.containsKey(r.getKey())) {
            r.repType = Request.ReplyType.INVALID_KEY;
            return;
        }
        ds.remove(r.getKey());
        r.repType = Request.ReplyType.OP_SUCCESS;
    }

    public void delete_all(Request r) {
        ds.clear();
        r.repType = Request.ReplyType.OP_SUCCESS;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
