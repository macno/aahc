package it.fluidware.aahc.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import it.fluidware.aahc.HTTP;
import it.fluidware.aahc.Response;
import it.fluidware.aahc.tools.HttpHeaderTool;

/**
 * Created by macno on 13/09/15.
 */
public abstract class FileResponse extends Response<FileOutputStream, File> {

    private File mFile;

    public FileResponse(File file) {
        mFile = file;
    }
    @Override
    protected FileOutputStream getOutputStream() {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(mFile);
        } catch (FileNotFoundException e) {

        }
        return out;
    }

    @Override
    protected File handle(FileOutputStream out) {
        Date lastModified = HttpHeaderTool.lastModified(mHeaders);
        if(lastModified != null) {
            mFile.setLastModified(lastModified.getTime());
        }
        return mFile;
    }

}
