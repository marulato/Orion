package org.orion.common.miscutil;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

public class IOUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static byte[] toByteArray(String filePath) throws Exception {
        if (!StringUtil.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isFile()) {
                FileInputStream inputStream = new FileInputStream(file);
                return IOUtils.toByteArray(inputStream);
            }
        }
        return null;
    }

    public static byte[] toByteArray(File file) throws Exception {
        if (file != null && file.isFile()) {
            FileInputStream inputStream = new FileInputStream(file);
            return IOUtils.toByteArray(inputStream);
        }
        return null;
    }

}
