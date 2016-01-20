Colin Stone 31645112

Port:
45112

Run:
java -jar -Xmx64m simpleKVStore.jar 45112

The server is running 2 active threads at start. The ListenerThread loops while receive packets from the UDP socket and constructs Message items to store in a shared queue. This ensures that the ListenerThread is consistently able to process requests without the overhead of creating a new thread everytime. The ServerThread will consume Message items from the shared queue and dispatch addtional threads that perform operations on the datastore. The datastore is backed by a ConcurrentHashMap. Threads for Messages created from a thread pool created from the newCachedThreadPool factory method. This thread pool reuses threads and is suited towards small asynchronous tasks.
