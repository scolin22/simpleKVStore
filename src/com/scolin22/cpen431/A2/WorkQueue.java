package com.scolin22.cpen431.A2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by colin on 2016-01-17.
 */
public class WorkQueue {
    final static int TIMEOUT = 1;

    ConcurrentHashMap<byte[], BlockingQueue> workQueue;

    public WorkQueue() {
        this.workQueue = new ConcurrentHashMap<byte[], BlockingQueue>();
    }

    public void storeRequest(Request r) {
        BlockingQueue queue = workQueue.get(r.UID);
        try {
            if (queue == null) {
                queue = new LinkedBlockingQueue<Request>();
                workQueue.put(r.UID, queue);
            }
            //TODO: offer
            queue.put(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Object nextRequest() {
        //TODO: proper priority policy
        for (byte[] key : workQueue.keySet()) {
            if (!workQueue.get(key).isEmpty()) {
                try {
                    return workQueue.get(key).poll(TIMEOUT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Object fetch(byte[] UID) {
        try {
            return workQueue.get(UID).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
