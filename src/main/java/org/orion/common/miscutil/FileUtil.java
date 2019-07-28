package org.orion.common.miscutil;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static final String B  = "B";
    public static final String KB = "KB";
    public static final String MB = "MB";
    public static final String GB = "GB";
    public static final String TB = "TB";
    private static final List<String> IMAGE_EXTEND  = CollectionUtil.unmutableList("JPG", "JPEG", "BMP", "PNG", "GIF", "RAW", "PSD", "TIFF", "WEBP");
    private static final List<String> VIDEO_EXTEND  = CollectionUtil.unmutableList("MP4", "AVI", "MKV", "FLV", "RMVB", "MOV", "WMV", "AVC", "264", "265");
    private static final List<String> DOC_EXTEND  = CollectionUtil.unmutableList("TXT", "DOC", "DOCX", "XLS", "XLSX", "PPT", "PPTX");
    private static final List<String> EXECUTABLE_EXTEND  = CollectionUtil.unmutableList("EXE", "BAT", "JAR", "SH", "COM", "JS", "VBS");

    public static boolean isImage(File file) {
        if (file != null && file.isFile()) {
            String exName = StringUtil.getFileSuffix(file.getName());
            if (IMAGE_EXTEND.contains(exName.toUpperCase())) {
                return true;
            }
        }
        return false;
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

    @Deprecated
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


    public static double getSize(File file, String unit) {
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

    public static void mkdirs(String dir) {
        if (!StringUtil.isEmpty(dir)) {
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    public static void cleanDir(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            FileUtils.cleanDirectory(dir);
        }
    }

    public static void copy(String src, String dest) throws IOException {
        copy(new File(src), new File(dest));
    }

    public static void copy(File src, File dest) throws IOException {
        if (src != null && dest != null) {
            if (src.isFile() && dest.exists() && dest.isDirectory()) {
                FileUtils.copyFileToDirectory(src, dest);
            } else if(src.isDirectory() && dest.exists() && dest.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(src, dest);
            } else if(src.isFile() && !dest.exists()) {
                FileUtils.copyFile(src, dest);
            } else if(src.isDirectory()) {
                FileUtils.copyDirectory(src, dest);
            }
        }
    }

    public static void move(String src, String dest) throws IOException {
        move(new File(src), new File(dest));
    }

    public static void move(File src, File dest) throws IOException {
        if (src != null && dest != null) {
            if (src.isFile() && dest.exists() && dest.isDirectory()) {
                FileUtils.moveFileToDirectory(src, dest, true);
            } else if(src.isDirectory() && dest.exists() && dest.isDirectory()) {
                FileUtils.moveDirectoryToDirectory(src, dest, true);
            } else if(src.isFile() && !dest.exists()) {
                FileUtils.moveFile(src, dest);
            } else if(src.isDirectory()) {
                FileUtils.moveDirectory(src, dest);
            }
        }
    }

}
