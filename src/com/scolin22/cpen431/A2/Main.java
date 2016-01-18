package com.scolin22.cpen431.A2;

public class Main {

    public static void main(String[] args) {
        if (args.length != 0) {
            System.out.println("Invalid number of arguments: e.g. java -jar simpleKVStore.jar");
            return;
        }

        KVStoreServer server = new KVStoreServer();
        server.start();
    }
}
