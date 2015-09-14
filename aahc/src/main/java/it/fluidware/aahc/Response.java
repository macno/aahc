package it.fluidware.aahc;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by macno on 12/09/15.
 */
public abstract class Response<S,T> {

    protected Request mRequest;

    protected Map<String, List<String>> mHeaders;

    public Response() {
    }

    protected void setRequest(Request request) {
        mRequest = request;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        mHeaders = headers;
    }

    protected abstract S getOutputStream() ;

    protected abstract T handle(S out);

    public abstract void done(T obj);

}
