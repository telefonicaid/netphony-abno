package es.tid.provisioningManager.modules.orchestrator;

import java.util.LinkedList;


public class OrchestratorQueue {

    private final int nThreads;
    private final OrchestratorThread[] threads;
    private final LinkedList<Orchestrator> queue;

    public OrchestratorQueue(int nThreads)
    {
        this.nThreads = nThreads;
        queue = new LinkedList<Orchestrator>();
        threads = new OrchestratorThread[nThreads];

        for (int i=0; i<nThreads; i++) {
            threads[i] = new OrchestratorThread();
            threads[i].start();
        }
    }

    public void execute(Orchestrator orchestrator) {
        synchronized(queue) {
            queue.addLast(orchestrator);
            queue.notify();
        }
    }

    private class OrchestratorThread extends Thread {
        public void run() {
        	Orchestrator orchestrator;

            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }

                    orchestrator = (Orchestrator) queue.removeFirst();
                }
                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                	orchestrator.start();
                }
                catch (RuntimeException e) {
                	/*FIXME*/
                    // You might want to log something here
                }
            }
        }
    }
}
