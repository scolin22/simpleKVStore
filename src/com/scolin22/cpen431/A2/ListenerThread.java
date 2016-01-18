package com.scolin22.cpen431.A2;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenerThread extends Thread {
    private static Logger log = Logger.getLogger(ListenerThread.class.getName());

    ApplicationLayer al;
    WorkQueue wq;

    private volatile boolean running = true;

    public ListenerThread(ApplicationLayer al, WorkQueue wq) {
        log.setLevel(Level.OFF);

        this.al = al;
        this.wq = wq;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            Request r = al.getRequest();
            if (r == null) continue;
            wq.storeRequest(r);
        }
    }
}
