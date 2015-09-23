package it.fluidware.aahc.impl;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import it.fluidware.aahc.Response;
import it.fluidware.aahc.tools.HttpHeaderTool;

/**
 * Created by macno on 13/09/15.
 */
public abstract class StringResponse extends Response<ByteArrayOutputStream, String> {

    @Override
    protected ByteArrayOutputStream getOutputStream() {
        return new ByteArrayOutputStream();
    }

    @Override
    protected String handle(ByteArrayOutputStream out) {

        String parsed;
        try {
            parsed = new String(out.toByteArray(), HttpHeaderTool.parseCharset(getHeaders()));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(out.toByteArray());
        }

        return parsed;

    }

}
