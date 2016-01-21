Colin Stone 31645112

Port:
45112

Run:
java -jar -Xmx64m simpleKVStore.jar 45112

The server is running 2 active threads at start. The ListenerThread loops while receive packets from the UDP socket and constructs Message items to dispatch additional threads that perform operations on the datastore. The datastore is backed by a ConcurrentHashMap. Threads for Messages created from a thread pool created from an Executor Service with a fixed size ArrayBlockingQueue. This is to ensure that my server will not run out memory while processing requests and storing values in Message objects.
