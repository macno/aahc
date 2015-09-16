package it.fluidware.aahc;

import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by macno on 14/09/15.
 */
public class Worker extends Thread {

    private PriorityBlockingQueue<Request> mQueue;

    private boolean mQuit = false;

    protected boolean busy = false;

    private int mId = -1;
    public Worker(PriorityBlockingQueue<Request> queue, int id) {
        Log.d(AAHC.NAME, "Created queue " + id);
        mQueue = queue;
        mId = id;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Request request;
        while (true) {
            try {

                Log.d(AAHC.NAME, "Worker " + mId + " ready");
                long startTimeMs = SystemClock.elapsedRealtime();
                // release previous request object to avoid leaking request object when mQueue is drained.
                request = null;
                try {
                    // Take a request from the queue.
                    request = mQueue.take();
                    busy = true;
                    Log.d(AAHC.NAME,"Worker " + mId + " busy");
                    request.doRequest(mId);
                } catch (InterruptedException e) {
                    // We may have been interrupted because it was time to quit.
                    if (mQuit) {
                        Log.d(AAHC.NAME,"Worker " + mId + " interruped");
                        return;
                    }
                    continue;
                }
            } finally {
                busy = false;
            }
        }

    }

    public void quit() {
        mQuit = true;
        interrupt();
    }
}
