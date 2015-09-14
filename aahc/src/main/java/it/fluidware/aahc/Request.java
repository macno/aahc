package it.fluidware.aahc;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.fluidware.aahc.tools.DateTool;

/**
 * Created by macno on 12/09/15.
 */
public class Request implements Comparable<Request> {

    private Client mClient;
    private URL mUrl;
    private HashMap<String, String> mHeaders = new HashMap<>();

    private Response mResponse;

    private int mWorker;

    protected AAHC.ErrorListener mErrorListener;
    protected AAHC.ProgressListener mProgressListener;


    public Request(Client client, URL url) {
        mClient = client;
        mUrl = url;
        mHeaders.put(HTTP.USER_AGENT,mClient.getUserAgent());
    }

    public Request ifModified(Date since) {
        mHeaders.put(HTTP.IF_MODIFIED_SINCE, DateTool.toRFC1123(since));
        return this;
    }

    public Request ifModified(long since) {
        return ifModified(new Date(since));
    }

    public Request accept(String accept) {
        mHeaders.put(HTTP.ACCEPT, accept);
        return this;
    }

    public void into(Response callback) {

        mResponse = callback;

        mClient.ready(this);
        // Ready to be handled
        // doRequest(callback);

    }

    public Request whileProgress(AAHC.ProgressListener callback) {
        mProgressListener = callback;
        return this;
    }

    public Request whenError(AAHC.ErrorListener callback) {
        mErrorListener = callback;
        return this;
    }


    // Protected methods

    protected void doRequest(int workerId) {
        mWorker = workerId;
        final Handler mainThread = new Handler(Looper.getMainLooper());

        mResponse.setRequest(this);

        final HttpURLConnection urlConnection = getConnection();

        if(urlConnection == null) {
            return;
        }

        try {
            int resCode = urlConnection.getResponseCode();

            Log.d(AAHC.NAME,"ResCode: " + resCode);

            // if not-modified
            if(mHeaders.containsKey(HTTP.IF_MODIFIED_SINCE)) {
                if(resCode == HTTP.STATUS.NOT_MODIFIED) {
                    // We have no body and we don't use cache, so caller must know we will pass a null object
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {

                            mResponse.done(null);
                        }
                    });

                    urlConnection.disconnect();
                    return;
                }
            }


        } catch (IOException e) {
            Log.e(AAHC.NAME,"IOException on getResponseCode",e);
            if (mErrorListener != null) {
                mErrorListener.onError(e);
            }

            urlConnection.disconnect();
            return;
        }

        handleConnection(urlConnection);

        try {
            InputStream input = new BufferedInputStream(urlConnection.getInputStream());

            mResponse.setHeaders(urlConnection.getHeaderFields());

            OutputStream output = (OutputStream) mResponse.getOutputStream();

            if (output == null) {
                // TODO Error!
                Log.e(AAHC.NAME, "output stream is null");
                if (mErrorListener != null) {
                    mErrorListener.onError(new IOException("output stream is null"));
                }
                return;
            }

            // get an channel from the stream
            final ReadableByteChannel inputChannel = Channels.newChannel(input);

            final WritableByteChannel outputChannel = Channels.newChannel(output);

            // copy the channels

            final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

            long reads = 0, read = 0;
            while ((read = inputChannel.read(buffer)) != -1) {
                // prepare the buffer to be drained
                buffer.flip();
                // write to the channel, may block
                outputChannel.write(buffer);
                // If partial transfer, shift remainder down
                // If buffer is empty, same as doing clear()
                buffer.compact();

                reads += read;
                if (mProgressListener != null) {

                    final long current = reads;
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressListener.progress(current);
                        }
                    });
                }

            }
            // EOF will leave buffer in fill state
            buffer.flip();
            // make sure the buffer is fully drained.
            while (buffer.hasRemaining()) {
                outputChannel.write(buffer);
            }

            // closing the channels
            inputChannel.close();
            outputChannel.close();

            if (mProgressListener != null) {

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressListener.complete();
                    }
                });
            }

            final Object obj = mResponse.handle(output);

            mainThread.post(new Runnable() {
                @Override
                public void run() {

                    mResponse.done(obj);
                }
            });


        } catch(final FileNotFoundException e) {
            Log.e(AAHC.NAME, e.toString(), e);
            if (mErrorListener != null) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        mErrorListener.onError(e);
                    }
                });
            }
        } catch (final IOException e) {
            Log.e(AAHC.NAME, e.toString(), e);
            if (mErrorListener != null) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        mErrorListener.onError(e);
                    }
                });
            }
        } finally {
            Log.d(AAHC.NAME,"URL disconnected " + urlConnection.getURL());
            urlConnection.disconnect();
        }

    }

    // Private methods


    private void handleConnection(HttpURLConnection urlConnection) {

        String s_contentLength  = urlConnection.getHeaderField("Content-Length");
        long contentLength = -1;
        if(s_contentLength != null) {
            try {
                contentLength = Long.parseLong(s_contentLength);
            } catch(NumberFormatException e) {
                // Malformed header...
            }
        }
        if(mProgressListener != null) {
            mProgressListener.total(contentLength);
        }
    }


    private HttpURLConnection getConnection() {
        if(mUrl == null) {
            if (mErrorListener != null) {
                mErrorListener.onError(new Exception("Invalid URL"));
            }
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) mUrl.openConnection();
            Log.d(AAHC.NAME, "[Worker " + mWorker+"] URL connected" + urlConnection.getURL());
            addHeaders(urlConnection);
        } catch (IOException e) {
            if (mErrorListener != null) {
                mErrorListener.onError(e);
            }
        }
        return urlConnection;
    }

    private void addHeaders(HttpURLConnection urlConnection) {
        for (Map.Entry<String, String> header: mHeaders.entrySet()) {
            urlConnection.setRequestProperty(header.getKey(),header.getValue());
        }
    }

    @Override
    public int compareTo(Request another) {
        return 0;
    }
}
