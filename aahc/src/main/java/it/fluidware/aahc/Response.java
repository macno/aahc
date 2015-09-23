package it.fluidware.aahc;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by macno on 12/09/15.
 */
public abstract class Response<S,T> {

    private int mCode;
    private String mMessage;

    protected Request mRequest;

    private Map<String, List<String>> mHeaders;

    public Response() {
    }

    public void setHeaders(Map<String, List<String>> headers) {
        mHeaders = headers;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    protected void setRequest(Request request) {
        mRequest = request;
    }

    protected abstract S getOutputStream() ;

    protected abstract T handle(S out);

    public abstract void done(T obj);

    protected Map<String, List<String>> getHeaders() {
        return mHeaders;
    }


}
