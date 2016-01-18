package com.scolin22.cpen431.A2;

import com.scolin22.cpen431.utils.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by colin on 2016-01-17.
 */
public class ServerThread extends Thread {
    private static Logger log = Logger.getLogger(ServerThread.class.getName());

    ApplicationLayer al;
    WorkQueue wq;
    DataStore ds;
    ExecutorService threadPool = Executors.newFixedThreadPool(20);

    private volatile boolean running = true;

    public ServerThread(ApplicationLayer al, WorkQueue wq, DataStore ds) {
        log.setLevel(Level.OFF);

        this.al = al;
        this.ds = ds;
        this.wq = wq;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        log.info("ServerThread started.");
        while (running) {
            Request r = (Request) wq.nextRequest();
            if (r == null) continue;
            log.info("PROCESD request, UID: " + StringUtils.byteArrayToHexString(r.UID));

            threadPool.execute(new WorkerRunnable(r, al, ds));
        }
    }
}
