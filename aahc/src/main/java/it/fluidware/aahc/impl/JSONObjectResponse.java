package it.fluidware.aahc.impl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import it.fluidware.aahc.AAHC;
import it.fluidware.aahc.Response;
import it.fluidware.aahc.tools.HttpHeaderTool;

/**
 * Created by macno on 12/09/15.
 */
public abstract class JSONObjectResponse  extends Response<ByteArrayOutputStream, JSONObject> {

    private static final String RESPONSE_CHARSET = "utf-8";
    @Override
    protected ByteArrayOutputStream getOutputStream() {
        return new ByteArrayOutputStream();
    }

    @Override
    protected JSONObject handle(ByteArrayOutputStream out) {

        String source;
        try {
            source = new String(out.toByteArray(), HttpHeaderTool.parseCharset(getHeaders(), RESPONSE_CHARSET));
        } catch (UnsupportedEncodingException e) {
            source = new String(out.toByteArray());
        }
        JSONObject ret = null;
        try {
            ret = new JSONObject(source);
        } catch (JSONException e) {

        }
        return ret;
    }

}