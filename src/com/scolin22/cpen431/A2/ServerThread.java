package com.scolin22.cpen431.A2;

import com.scolin22.cpen431.utils.StringUtils;

import java.util.logging.Logger;

/**
 * Created by colin on 2016-01-17.
 */
public class ServerThread extends Thread {
    private static Logger log = Logger.getLogger(ServerThread.class.getName());

    ApplicationLayer al;
    WorkQueue wq;
    DataStore ds;

    private volatile boolean running = true;

    public ServerThread(ApplicationLayer al, WorkQueue wq, DataStore ds) {
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
            new Thread(new WorkerThread(r, al, ds)).start();
        }
    }
}
