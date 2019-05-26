package org.orion.common.miscutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class IOUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public static void close(Reader in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public static void close(OutputStream out) {
        if (out != null) {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public static void close(Writer out) {
        if (out != null) {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public static FileInputStream getFileInputStream(String path) {
        FileInputStream fileInputStream = null;
        try {
            if (!StringUtil.isEmpty(path))
                fileInputStream = new FileInputStream(new File(path));
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            return fileInputStream;
        }
    }

    public static FileOutputStream getFileOutputStream(String path) {
        FileOutputStream fileOutputStream = null;
        try {
            if (!StringUtil.isEmpty(path))
                fileOutputStream = new FileOutputStream(new File(path));
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            return fileOutputStream;
        }
    }

    public static byte[] generateByteArray(File file) {
        byte[] data = null;
        if (file != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                fis.close();
                bos.close();
                data = bos.toByteArray();
            } catch (Exception e) {
                logger.error("An Exception Occurred While Generate Byte Array", e);
            }

        }
        return data;
    }

    public static void write(String src) {

    }
}
