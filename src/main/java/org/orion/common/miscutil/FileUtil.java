package org.orion.common.miscutil;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static final String B  = "B";
    public static final String KB = "KB";
    public static final String MB = "MB";
    public static final String GB = "GB";
    public static final String TB = "TB";

    public static File createDirectory(String path) {
        if (StringUtil.isEmpty(path))
            return null;
        File file = new File(path);
        if (! file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean deleteSingleFile(String path) {
        if (StringUtil.isEmpty(path))
            return false;
        File file = new File(path);
        if (file.isDirectory() || ! file.exists()) {
            return false;
        } else {
            return file.delete();
        }
    }

    public static int deleteAll(String path) {
        if (StringUtil.isEmpty(path))
            return -1;
        File file = new File(path);
        int count = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (File childFile : fileList) {
                    if (childFile.isDirectory()) {
                        deleteAll(childFile.getAbsolutePath());
                    } else {
                        childFile.delete();
                        count ++;
                    }
                }
                file.delete();
                count ++;
            } else {
                file.delete();
                count ++;
            }
        } else {
            logger.warn("The File Path Dose NOT Exsit -> " + path);
            count = -1;
        }
        return count;
    }

    public static int deleteFile(String path) {
        if (StringUtil.isEmpty(path))
            return -1;
        File file = new File(path);
        int count = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (File childFile : fileList) {
                    if (childFile.isDirectory()) {
                        deleteFile(childFile.getAbsolutePath());
                    } else {
                        childFile.delete();
                        count ++;
                    }
                }
            } else {
                file.delete();
                count ++;
            }
        } else {
            logger.warn("The File Path Dose NOT Exsit -> " + path);
            count = -1;
        }
        return count;
    }

    public static void deleteFileWithNameHasString(String path, String contain) {
        if (path.isBlank() || contain.isBlank())
            return;
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (File childFile : fileList) {
                    if (childFile.isDirectory()) {
                        deleteFileWithNameHasString(childFile.getAbsolutePath(), contain);
                    } else {
                        if (childFile.getName().contains(contain))
                            childFile.delete();
                    }
                }
            } else {
                if (file.getName().contains(contain))
                    file.delete();
            }
        }
    }

    public static void deleteSingleFile(String path, String fileName) {
        if (!StringUtil.isBlank(fileName) && StringUtil.isBlank(path)) {
            if (path.endsWith("/") || path.endsWith("\\")) {
                path = path + fileName;
            } else {
                if (path.contains("/"))
                    path += "/" + fileName;
                if (path.contains("\\"))
                    path += "\\" + fileName;
            }
            deleteSingleFile(path);
        }
    }

    public static double getSize(String path, String unit) {
        if (StringUtil.isEmpty(path))
            return 0;
        File file = new File(path);
        if (file.exists()) {
            double size = file.length();
            if (KB.equalsIgnoreCase(unit)) {
                size =size / 1024;
            } else if (MB.equalsIgnoreCase(unit)) {
                size = size / Math.pow(1024, 2);
            } else if(GB.equalsIgnoreCase(unit)) {
                size = size / Math.pow(1024, 3);
            } else if (TB.equalsIgnoreCase(unit)) {
                size = size / Math.pow(1024, 4);
            }
            return size;
        } else {
            return 0;
        }
    }

}
