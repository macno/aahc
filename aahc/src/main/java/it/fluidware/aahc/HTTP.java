package it.fluidware.aahc;

/**
 * Created by macno on 13/09/15.
 *
 * From org.apache.http.protocol.HTTP
 */


public final class HTTP {
    public static final String ASCII = "ASCII";
    public static final String CHARSET_PARAM = "; charset=";
    public static final String CHUNK_CODING = "chunked";
    public static final String CONN_CLOSE = "Close";
    public static final String CONN_DIRECTIVE = "Connection";
    public static final String CONN_KEEP_ALIVE = "Keep-Alive";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_LEN = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final int CR = 13;
    public static final String DATE_HEADER = "Date";
    public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    public static final String DEFAULT_PROTOCOL_CHARSET = "US-ASCII";
    public static final String EXPECT_CONTINUE = "100-continue";
    public static final String EXPECT_DIRECTIVE = "Expect";
    public static final int HT = 9;
    public static final String IDENTITY_CODING = "identity";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final int LF = 10;
    public static final String OCTET_STREAM_TYPE = "application/octet-stream";
    public static final String PLAIN_TEXT_TYPE = "text/plain";
    public static final String SERVER_HEADER = "Server";
    public static final int SP = 32;
    public static final String TARGET_HOST = "Host";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String USER_AGENT = "User-Agent";
    public static final String US_ASCII = "US-ASCII";
    public static final String UTF_16 = "UTF-16";
    public static final String UTF_8 = "UTF-8";

    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String ACCEPT = "Accept";


    HTTP() {
        throw new RuntimeException("Stub!");
    }

    public static boolean isWhitespace(char ch) {
        throw new RuntimeException("Stub!");
    }

    public interface STATUS {

        //  Informational 1xx
        int CONTINUE = 100;
        int SWITCHING_PROTOCOLS = 101;
        int PROCESSING = 102;

        // Successful 2xx
        int OK = 200;
        int CREATED = 201;
        int ACCEPTED = 202;
        int NON_AUTHORITATIVE_INFORMATION = 203;
        int NO_CONTENT = 204;
        int RESET_CONTENT = 205;
        int PARTIAL_CONTENT = 206;
        int MULTI_STATUS = 207;

        // Redirection 3xx
        int MULTIPLE_CHOICES = 300;
        int MOVED_PERMANENTLY = 301;
        int MOVED_TEMPORARILY = 302;
        int SEE_OTHER = 303;
        int NOT_MODIFIED = 304;
        int USE_PROXY = 305;
        int TEMPORARY_REDIRECT = 307;

        // Client Error 4xx
        int BAD_REQUEST = 400;
        int UNAUTHORIZED = 401;
        int PAYMENT_REQUIRED = 402;
        int FORBIDDEN = 403;
        int NOT_FOUND = 404;
        int METHOD_NOT_ALLOWED = 405;
        int NOT_ACCEPTABLE = 406;
        int PROXY_AUTHENTICATION_REQUIRED = 407;
        int REQUEST_TIMEOUT = 408;
        int CONFLICT = 409;
        int GONE = 410;
        int LENGTH_REQUIRED = 411;
        int PRECONDITION_FAILED = 412;
        int REQUEST_TOO_LONG = 413;
        int REQUEST_URI_TOO_LONG = 414;
        int UNSUPPORTED_MEDIA_TYPE = 415;
        int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
        int EXPECTATION_FAILED = 417;
        int INSUFFICIENT_SPACE_ON_RESOURCE = 419;
        int METHOD_FAILURE = 420;
        int UNPROCESSABLE_ENTITY = 422;
        int LOCKED = 423;
        int FAILED_DEPENDENCY = 424;

        // Server Error 5xx
        int INTERNAL_SERVER_ERROR = 500;
        int NOT_IMPLEMENTED = 501;
        int BAD_GATEWAY = 502;
        int SERVICE_UNAVAILABLE = 503;
        int GATEWAY_TIMEOUT = 504;
        int HTTP_VERSION_NOT_SUPPORTED = 505;
        int INSUFFICIENT_STORAGE = 507;
    }
}

