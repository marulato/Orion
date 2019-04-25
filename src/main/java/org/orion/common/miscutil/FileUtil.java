package org.orion.common.miscutil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;


public class FileUtil {
	private static final Logger logger = LogManager.getLogger(FileUtil.class);
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

	
	public static FileInputStream getInputStream(String path) {
		if (StringUtil.isEmpty(path))
			return null;
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			logger.error("An Exception Occurred while get InputStream", e);
		}
		return stream;
	}

	public static FileOutputStream getOutputStream(String path) {
		if (StringUtil.isEmpty(path))
			return null;
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			logger.error("An Exception Occurred while get OutputStream", e);
		}
		return stream;
	}
	public static String getFileSuffix(String fullfileName) {
		if (StringUtil.isEmpty(fullfileName) || !fullfileName.contains(".")) {
			return null;
		}
		return fullfileName.substring(fullfileName.lastIndexOf(".")+1);
	}
	
	public static String getFilePrefix(String fullfileName) {
		if (StringUtil.isEmpty(fullfileName) || !fullfileName.contains(".")) {
			return fullfileName;
		}
		return fullfileName.substring(0,fullfileName.lastIndexOf("."));
	}
	
	public static String getFileNameFromPath(String path) {
		if (StringUtil.isEmpty(path)) {
			return null;
		}
		String[] splitStr = null;
		if (path.contains("/")) {			
			splitStr = path.split("/");
		} else if (path.contains("\\")) {
			splitStr = path.split("\\\\");
		}
		if (splitStr != null && splitStr.length > 0) {
			return splitStr[splitStr.length - 1];
		} else {
			return null;
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

}
