package it.fluidware.aahc.impl;

import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import it.fluidware.aahc.AAHC;
import it.fluidware.aahc.Response;
import it.fluidware.aahc.tools.HttpHeaderTool;

/**
 * Created by macno on 13/09/15.
 */
public abstract class DOMResponse extends Response<FileOutputStream, Document> {

    private static final String FILE_PREFIX = "domxml";
    private File mTmpFile;

    @Override
    protected FileOutputStream getOutputStream() {
        FileOutputStream out = null;
        try {
            mTmpFile = File.createTempFile(FILE_PREFIX, String.valueOf(System.nanoTime()));
            if(mTmpFile != null) {
                out = new FileOutputStream(mTmpFile);
            }
        } catch(IOException e) {
            Log.e(AAHC.NAME,e.toString(),e);
        }
        return out;
    }

    @Override
    protected Document handle(FileOutputStream out) {
        Document d = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            d = db.parse(mTmpFile);
        } catch (ParserConfigurationException e) {
            Log.e(AAHC.NAME, e.toString(), e);
        } catch (SAXException e) {
            Log.e(AAHC.NAME, e.toString(), e);
        } catch (IOException e) {
            Log.e(AAHC.NAME, e.toString(), e);
        }

        return d;

    }

}
