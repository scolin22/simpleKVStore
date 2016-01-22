Colin Stone 31645112

Port:
45112

Run:
java -jar -Xmx64m simpleKVStore.jar 45112

The server is running 2 active threads at start. The ListenerThread loops while receive packets from the UDP socket and constructs Message items to dispatch additional threads that perform operations on the datastore. The datastore is backed by a ConcurrentHashMap. Threads for Messages created from a thread pool created from an Executor Service with a fixed size ArrayBlockingQueue. This is to ensure that my server will not run out memory while processing requests and storing values in Message objects.

In addition to the provided tests, a test was conducted to overload the server. This test performed as many 10KB PUT requests as possible without respecting the server's NO_SPACE response. This test exposed a flaw in the original server utilizing a CachedThreadPool, causing an OutOfMemoryError and crashing the server. The server would continue processing requests and queueing up WorkerRunnables. The improvement was to use a custom thread pool with a fixed bound blockingqueue which silently dropped tasks beyond a certain threshold (20 tasks queued).
