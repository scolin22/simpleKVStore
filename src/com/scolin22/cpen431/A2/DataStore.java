package com.scolin22.cpen431.A2;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataStore {
    final static int CAPACITY = 100000;
    private static Logger log = Logger.getLogger(DataStore.class.getName());
    ConcurrentHashMap<ByteBuffer, byte[]> ds;

    public DataStore() {
        log.setLevel(Level.INFO);

        this.ds = new ConcurrentHashMap<ByteBuffer, byte[]>();
    }

    public void put(Request r) {
        double heapFreeSize = Runtime.getRuntime().freeMemory();
        double heapMaxSize = Runtime.getRuntime().maxMemory();

        if (heapFreeSize / heapMaxSize < 0.1 || ds.size() >= CAPACITY) {
            log.info("Exceeded heap limit Free: " + heapFreeSize + " Max: " + heapMaxSize);
            r.repType = Request.ReplyType.NO_SPACE;
        } else if (r.length < 0 || r.length > Request.MAX_VAL_LENGTH) {
            r.repType = Request.ReplyType.BAD_VAL_LEN;
        } else {
            ds.put(r.getKey(), r.value);
            r.repType = Request.ReplyType.OP_SUCCESS;
        }
    }

    public void get(Request r) {
        if (!ds.containsKey(r.getKey())) {
            r.repType = Request.ReplyType.INVALID_KEY;
        } else {
            byte[] store = ds.get(r.getKey());
            r.length = (short) store.length;
            r.value = store;
            r.repType = Request.ReplyType.OP_SUCCESS;
        }
    }

    public void remove(Request r) {
        if (!ds.containsKey(r.getKey())) {
            r.repType = Request.ReplyType.INVALID_KEY;
        } else {
            ds.remove(r.getKey());
            r.repType = Request.ReplyType.OP_SUCCESS;
        }
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
