package it.fluidware.aahc;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by macno on 12/09/15.
 */
public class Client {

    private Context mContext;
    private String mUserAgent;

    private static final int MAX_LOOP = 5;

    private static final int DEFAULT_POOL_SIZE = 4;

    private final PriorityBlockingQueue<Request> mQueue =
            new PriorityBlockingQueue<Request>();

    private Worker[] mWorkers;
    private int mPoolSize;

    private int mMaxLoop = MAX_LOOP;

    protected Client(Context context) {
        this(context, null);
    }

    protected Client(Context context, CookieManager cookieManager) {
        this(context, cookieManager, DEFAULT_POOL_SIZE);
    }

    protected Client(Context context, CookieManager cookieManager, int poolSize) {
        if (cookieManager != null) {
            CookieHandler.setDefault(cookieManager);
        }
        mContext = context;
        mPoolSize = poolSize;
        mWorkers = new Worker[mPoolSize];
        Log.d("AAHC", "initializeWorkers()");
        initializeWorkers();
    }

    protected Context getContext() {
        return mContext;
    }

    protected String getUserAgent() {
        if (mUserAgent == null) setDefaultUserAgent();
        return mUserAgent;
    }

    protected synchronized void ready(Request request) {
        Log.d(AAHC.NAME, "Queing request");
        mQueue.add(request);
        initializeWorkers();
    }

    /* Private methods */

    private void initializeWorkers() {
        for (int i = 0; i < mWorkers.length; i++) {
            if (mWorkers[i] == null) {
                mWorkers[i] = new Worker(mQueue, i);
                mWorkers[i].start();
            } else {

                if (mWorkers[i].isInterrupted()) {
                    mWorkers[i] = new Worker(mQueue, i);
                    mWorkers[i].start();
                }
            }

            if (!mWorkers[i].busy) {
                break;
            }
        }
    }

    private void setDefaultUserAgent() {
        mUserAgent = AAHC.NAME + "/" + AAHC.VERSION + " " +
                " (Linux; U; Android " + Build.VERSION.RELEASE +
                "; " + Build.MODEL +
                " Build/" + Build.ID + ")";
    }



    /* Public methods */

    public Client as(String userAgent) {
        mUserAgent = userAgent;
        return this;
    }

    public int getMaxLoop() {
        return mMaxLoop;
    }

    public Request toGet(String url) {
        return new Request(this, Request.GET, url);
    }

    public Request toHead(String url) {
        return new Request(this, Request.HEAD, url);
    }

    public Request toPost(String url,String contentType, String data) {
        return doRequestWithBody(Request.POST, url, contentType, data);
    }

    public Request toPost(String url, boolean multipart, Map<String, ?> data) {
        return doRequestWithBody(Request.POST, url, multipart, data);
    }

    public Request toPost(String url,String contentType, InputStream is) {
        return doRequestWithBody(Request.POST, url, contentType, is);
    }

    public Request toPatch(String url,String contentType, String data) {
        return doRequestWithBody(Request.PATCH, url, contentType, data);
    }

    public Request toPatch(String url, boolean multipart, Map<String, ?> data) {
        return doRequestWithBody(Request.PATCH, url, multipart, data);
    }

    public Request toPatch(String url,String contentType, InputStream is) {
        return doRequestWithBody(Request.PATCH, url, contentType, is);
    }

    public Request toPut(String url,String contentType, String data) {
        return doRequestWithBody(Request.PUT, url, contentType, data);
    }

    public Request toPut(String url, boolean multipart, Map<String, ?> data) {
        return doRequestWithBody(Request.PUT, url, multipart, data);
    }

    public Request toPut(String url,String contentType, InputStream is) {
        return doRequestWithBody(Request.PUT, url, contentType, is);
    }

    public Request toDelete(String url) {
        return new Request(this, Request.DELETE, url);
    }

    public Request toDelete(String url,String contentType, String data) {
        return doRequestWithBody(Request.DELETE, url, contentType, data);
    }

    public Request toDelete(String url, boolean multipart, Map<String, ?> data) {
        return doRequestWithBody(Request.DELETE, url, multipart, data);
    }

    public Request toDelete(String url,String contentType, InputStream is) {
        return doRequestWithBody(Request.DELETE, url, contentType, is);
    }

    private Request doRequestWithBody(String method, String url,String contentType, String data) {
        Request r = new Request(this, method, url);
        r.setDoOutput(true);
        r.setPostBody(contentType, data);
        return r;
    }

    public Request doRequestWithBody(String method, String url,String contentType, InputStream is) {
        Request r = new Request(this, method, url);
        r.setDoOutput(true);
        r.setInputStream(contentType,is);
        return r;
    }

    public Request doRequestWithBody(String method, String url, boolean multipart, Map<String, ?> data) {
        Request r = new Request(this, method, url);
        r.setDoOutput(true);
        r.setData(multipart, data);
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
