package it.fluidware.aahc.tools;

import android.util.Log;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import it.fluidware.aahc.HTTP;

/**
 * Created by macno on 13/09/15.
 */
public final class HttpHeaderTool {

    private static String getHeader(String key, Map<String, List<String>> headers) {
        String header = null;
        List<String> entries = headers.get(key);
        if(entries != null) {
            header = entries.get(entries.size()-1);
        }

        return header;
    }


    /**
     * Retrieve a charset from headers
     *
     * @param headers An {@link java.util.Map} of headers
     * @param defaultCharset Charset to return if none can be found
     * @return Returns the charset specified in the Content-Type of this header,
     * or the defaultCharset if none can be found.
     */
    public static String parseCharset(Map<String, List<String>> headers, String defaultCharset) {

        String contentType = getHeader(HTTP.CONTENT_TYPE, headers);
        if (contentType != null) {
            String[] params = contentType.split(";");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }

        return defaultCharset;
    }

    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String parseCharset(Map<String, List<String>> headers) {
        return parseCharset(headers, HTTP.DEFAULT_CONTENT_CHARSET);
    }

    public static Date lastModified(Map<String, List<String>> headers) {
        Date d = null;
        String lastModified = getHeader(HTTP.LAST_MODIFIED, headers);
        if (lastModified != null) {
            try {
                d = DateTool.fromRFC1123(lastModified);
            } catch(ParseException e) {
                Log.e("AAHC",e.toString());
            }
        }
        return d;
    }

    public static long contentLength(Map<String, List<String>> headers) {
        long ret = -1;
        String contentLength = getHeader(HTTP.CONTENT_LEN,headers);
        if(contentLength != null) {
            ret = Long.parseLong(contentLength);
        }
        return ret;
    }
}
