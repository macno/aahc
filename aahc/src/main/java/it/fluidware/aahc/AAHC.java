package it.fluidware.aahc;

import android.content.Context;

/**
 * Created by macno on 12/09/15.
 */
public class AAHC {


    public static final String NAME = "AAHC";
    public static final String VERSION = "0.1.0";


    private static volatile Client sSingleton = null;

    public static Client use (Context context) {

        if(sSingleton == null) {
            synchronized (AAHC.class) {
                sSingleton = new Client(context);
            }
        }
        return sSingleton;
    }

    private AAHC() {

    }

    public interface ErrorListener {
        public void onError(Exception e);
    }

    public interface ProgressListener {
        public void total(long bytes);
        public void progress(long read);
        public void complete();
    }

}
