package it.fluidware.aahc.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by macno on 12/09/15.
 */
public final class DateTool {

    private static final String DATE_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss z";

    public static String toRFC1123(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DATE_RFC1123, Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(d);
    }

    public static Date fromRFC1123(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DATE_RFC1123, Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.parse(date);
    }
}
