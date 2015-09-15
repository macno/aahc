package it.fluidware.aahc.tools;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by macno on 15/09/15.
 */
public final class ProtocolTool {

    public static URL getAbsoluteURL(URL from, String relative) throws MalformedURLException {
        URL abs = null;

        if(relative.contains("://")) {
            if(relative.indexOf("://") == 0) {
                // Get protocol from "from"
                relative = from.getProtocol()+relative;
            }
            abs = new URL(relative);
        } else {
            abs = new URL(from, relative);
        }

        return abs;
    }


}
