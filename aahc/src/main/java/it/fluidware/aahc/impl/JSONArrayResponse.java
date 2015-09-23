package it.fluidware.aahc.impl;

import org.json.JSONArray;
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
public abstract class JSONArrayResponse  extends Response<ByteArrayOutputStream, JSONArray> {

    private static final String RESPONSE_CHARSET = "utf-8";
    @Override
    protected ByteArrayOutputStream getOutputStream() {
        return new ByteArrayOutputStream();
    }

    @Override
    protected JSONArray handle(ByteArrayOutputStream out) {

        String source;
        try {
            source = new String(out.toByteArray(), HttpHeaderTool.parseCharset(getHeaders(), RESPONSE_CHARSET));
        } catch (UnsupportedEncodingException e) {
            source = new String(out.toByteArray());
        }
        JSONArray ret = null;
        try {
            ret = new JSONArray(source);
        } catch (JSONException e) {

        }
        return ret;
    }

}