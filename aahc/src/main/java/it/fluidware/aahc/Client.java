package it.fluidware.aahc;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by macno on 12/09/15.
 */
public class Client {

    private Context mContext;
    private String mUserAgent;

    private static final int DEFAULT_POOL_SIZE = 4;

    private final PriorityBlockingQueue<Request> mQueue =
            new PriorityBlockingQueue<Request>();

    private Worker[] mWorkers;
    private int mPoolSize;


    protected Client(Context context) {
        this(context,DEFAULT_POOL_SIZE);
    }

    protected Client(Context context,int poolSize) {
        mContext = context;
        mPoolSize = poolSize;
        mWorkers = new Worker[mPoolSize];
        initializeWorkers();
    }

    protected Context getContext() {
        return mContext;
    }

    protected String getUserAgent() {
        if(mUserAgent == null) setDefaultUserAgent();
        return mUserAgent;
    }

    protected synchronized void ready(Request request) {
        Log.d(AAHC.NAME, "Queing request");
        mQueue.add(request);
        initializeWorkers();
    }

    /* Private methods */

    private void initializeWorkers() {
        for(int i=0;i< mWorkers.length;i++) {
            if(mWorkers[i] == null) {
                mWorkers[i] = new Worker(mQueue, i);
                mWorkers[i].start();
            }
        }
    }
    private void setDefaultUserAgent() {
        mUserAgent = AAHC.NAME+"/"+AAHC.VERSION + " "+
            " (Linux; U; Android " + Build.VERSION.RELEASE +
                "; " + Build.MODEL +
                " Build/"+Build.ID+")";
    }



    /* Public methods */

    public Client as(String userAgent) {
        mUserAgent = userAgent;
        return this;
    }

    public Request toGet(String url) throws MalformedURLException {

        Request r = new Request(this, new URL(url));

        return r;
    }

    public void clear() {
        mQueue.clear();
        for(int i=0;i< mWorkers.length;i++) {
            if(mWorkers[i] != null) {
                mWorkers[i].quit();
            }
        }
    }

}
