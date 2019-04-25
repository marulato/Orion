package org.orion.common.miscutil;

import java.io.*;

public class IOUtil {

    public static void close(InputStream in) throws IOException {
        if (in != null) {
            in.close();
        }
    }

    public static void close(Reader in) throws IOException {
        if (in != null) {
            in.close();
        }
    }

    public static void close(OutputStream out) throws IOException {
        if (out != null) {
            out.flush();
            out.close();
        }
    }

    public static void close(Writer out) throws IOException {
        if (out != null) {
            out.flush();
            out.close();
        }
    }
}
