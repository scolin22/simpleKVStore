package com.scolin22.cpen431.A2;

public class KVStoreServer {
    ApplicationLayer al;
    ListenerThread lt;
    ServerThread st;

    WorkQueue wq;
    DataStore ds;

    public KVStoreServer() {
        wq = new WorkQueue();
        ds = new DataStore();

        al = new ApplicationLayer(this);
        lt = new ListenerThread(al, wq);
        st = new ServerThread(al, wq, ds);
    }

    public void start() {
        lt.start();
        st.start();
    }

    public void shutdown() {
        //TODO: gracefully wait for threads to clean up and join
        lt.terminate();
        st.terminate();
        al.shutdown();
    }
}
