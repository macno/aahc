package it.fluidware.aahc.impl;

import java.io.IOException;
import java.io.OutputStream;

import it.fluidware.aahc.Response;

/**
 * Created by macno on 13/09/15.
 */
public abstract class EmptyResponse extends Response<EmptyResponse.NullOutputStream, Boolean> {

    @Override
    protected NullOutputStream getOutputStream() {
        return new NullOutputStream();
    }

    @Override
    protected Boolean handle(NullOutputStream out) {

        return true;

    }

    public class NullOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
        }
    }

}
