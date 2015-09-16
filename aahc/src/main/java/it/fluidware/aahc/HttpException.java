package it.fluidware.aahc;

import java.io.IOException;

/**
 * Created by macno on 15/09/15.
 */
public class HttpException extends IOException {
    private int code;
    private String message;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

}
