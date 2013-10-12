package ch.ethz.asl.message.middleware.pool;

import ch.ethz.asl.message.middleware.reactor.EventHandler;
import ch.ethz.asl.message.middleware.reactor.x.XEventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple worker pool
 */
public class WorkerPool {

    private List<XEventHandler> workers;

    private int nrThreads;

    private int next;

    public WorkerPool(int nrThreads) throws IOException {
        this.nrThreads = nrThreads;
        this.configurePool();
        this.initializeNextThread();
    }

    public void configurePool() throws IOException {
        workers = new ArrayList<>(nrThreads);

        for (int i = 0; i < nrThreads; i++) {
            workers.add(new XEventHandler());
        }
    }

    public void star() {
        for (XEventHandler worker : workers) {
            worker.start();
        }
    }

    public XEventHandler getWorker() {
        final XEventHandler t = workers.get(next);
        determineNextThread();
        return t;
    }

    public void initializeNextThread() {
        this.next = 0;
    }

    public void determineNextThread() {
        this.next = (++next) % nrThreads;
    }

}
