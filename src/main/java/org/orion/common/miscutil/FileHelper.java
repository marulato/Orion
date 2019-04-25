package org.orion.common.miscutil;

//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

	private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

	public static final String SYSTEM_TEMP_PATH = System.getProperty("java.io.tmpdir") + "MCPS_TEMP" + File.separator;

	public String[] getFilenames(String folerPath) {
		List<String> fileNames = new ArrayList<String>();
		File folder = new File(folerPath);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null && listOfFiles.length > 0) {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					fileNames.add(listOfFiles[i].getName());
				}
			}
		}
		return fileNames.size() == 0 ? null : fileNames.toArray(new String[fileNames.size()]);
	}

	public String appendFileName(String url, String baseName) {
		return (new StringBuilder()).append(url).append(System.getProperty("file.separator")).append(baseName).toString();
	}

	public static File getFileFromBytes(byte[] b, String outputFile) throws Exception {
		FileOutputStream fstream = null;
		BufferedOutputStream stream = null;
		File file = null;
		try {
			if (outputFile == null) {
				outputFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + System.currentTimeMillis() + ".pdf";
			}
			file = new File(outputFile);

			boolean isSuccess = true;
			while (file.exists()) {
				//file = new File(getTempFilename(outputFile));
			}
			File parent = file.getParentFile();
			if (!parent.exists()) {
				isSuccess = parent.mkdirs();
			}
			if (!isSuccess) {
				throw new IOException("No file careated --- " + outputFile);
			}
			isSuccess = file.createNewFile();
			if (!isSuccess) {
				throw new IOException("No file careated --- " + outputFile);
			}
			fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			logger.error("An exceptin occurred on 'getFileFromBytes'.", e);
			throw e;
		} finally {
			closeOutputStream(stream);
			closeOutputStream(fstream);
		}
		return file;
	}


	public void delete(String file) {
		logger.debug("######### will delete file :" + file + " ###############");
		File f = new File(file);
		if (f.isFile()) {
			if (f.exists()) {
				if (f.delete()) {
					logger.debug("######### file deleted successfully  ###############");
				}
			}
		}
	}

	public boolean deleteFolder(String folderPath) {
		File dir = new File(folderPath);
		return deleteDir(dir);
	}

	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			if (children != null && children.length > 0) {
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}
			// The directory is now empty so delete it
			return dir.delete();
		} else {
			return dir.delete();
		}
	}

	public static void closeInputStream(InputStream inputStream) {
		if (inputStream == null) {
			return;
		}

		try {
			inputStream.close();
		} catch (Exception e) {
			logger.error("Faile to close inputStream: " + e.getMessage());
		}
	}

	public static void closeOutputStream(OutputStream outputStream) {
		if (outputStream == null) {
			return;
		}

		try {
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			logger.error("Faile to close outputStream: " + e.getMessage());
		}
	}

	public static void closeReader(Reader reader) {
		if (reader == null) {
			return;
		}

		try {
			reader.close();
		} catch (Exception e) {
			logger.error("Faile to close reader: " + e.getMessage());
		}
	}

	public static void closePdfReader(PdfReader pdfReader) {
		if (pdfReader == null) {
			return;
		}

		try {
			pdfReader.close();
		} catch (Exception e) {
			logger.error("Faile to close reader: " + e.getMessage());
		}
	}

	public static void closeWriter(PdfWriter writer) {
		if (writer == null) {
			return;
		}

		try {
			writer.close();
		} catch (Exception e) {
			logger.error("Faile to close writer: " + e.getMessage());
		}
	}

	public static void closeDocument(Document document) {
		if (document == null) {
			return;
		}

		try {
			document.close();
		} catch (Exception e) {
			logger.error("Faile to close document: " + e.getMessage());
		}
	}


	public static String getTempFilename(String filename, String dummyString) {
		if (StringUtil.isEmpty(filename) || StringUtil.isEmpty(dummyString)) {
			return filename;
		}

		StringBuilder sbud = new StringBuilder();
		String extension = "";
		if (filename.contains(".")) {
			extension = filename.substring(filename.lastIndexOf("."));
		}
		int len = extension.length();
		if (len > 0) {
			sbud.append(filename.substring(0, filename.length() - extension.length()));
		} else {
			sbud.append(filename);
		}
		sbud.append(".").append(dummyString).append(extension);
		return sbud.toString();
	}

	/**
	 * 
	 * Get file from remote pc
	 * 
	 * @author jianwen
	 * @param remoteUrl
	 * @param localDir
	 */
	// public static void smbGet(String remoteUrl, String localDir) {
	// logger.debug("#######################Get file from remote pc Start###########################");
	// InputStream in = null;
	// OutputStream out =
	// null;
	// try {
	// SmbFile remoteFile = new SmbFile(remoteUrl);
	// if (remoteFile == null) {
	// logger.debug("File not exist");
	// return;
	// }
	// String fileName =
	// remoteFile.getName();
	// logger.debug("");
	// File localFile = new File(localDir + File.separator + fileName);
	// in = new BufferedInputStream(new
	// SmbFileInputStream(remoteFile));
	// out = new BufferedOutputStream(new FileOutputStream(localFile));
	// int length =
	// remoteFile.getContentLength();// get the file length byte buffer[] = new byte[length]; while (in.read(buffer) != -1) {
	// // out.write(buffer); }
	//
	// logger.debug("#######################Get file from remote pc End###########################");
	// } catch (Exception e) {
	// logger.error("There is a exception in this method:", e);
	// } finally {
	// try {
	// out.close();
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * get the file from remote pc and put it to the temp folder
	 * 
	 * @param remoteUrl
	 */
	// public static void smbGet(String remoteUrl) {
	// logger.debug("#######################Get file from remote pc End###########################");
	// InputStream in = null;
	// OutputStream out = null;
	// try {
	// SmbFile remoteFile = new SmbFile(remoteUrl);
	// if (remoteFile == null) {
	// logger.debug("File not exist");
	// return;
	// }
	// String fileName =
	// remoteFile.getName();
	// File localFile = new File(system_temp_path + File.separator + fileName);
	// in = new BufferedInputStream(new
	// SmbFileInputStream(remoteFile));
	// out = new BufferedOutputStream(new FileOutputStream(localFile));
	// int length =
	// remoteFile.getContentLength();// get the file length byte buffer[] = new byte[length]; while (in.read(buffer) != -1) {
	// // out.write(buffer); }
	//
	// logger.debug("#######################Get file from remote pc End###########################");
	// } catch (Exception e) {
	// logger.error("There is a exception in this method:", e);
	// } finally {
	// try {
	// out.close();
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * send the file to the remote pc
	 * 
	 * @param remoteUrl
	 *            -- smb://administrator:password$1@192.168.6.45/test/test.txt"
	 * @param localFilePath
	 *            --
	 */

	// public static void smbPut(String remoteUrl, String localFilePath) {
	// InputStream in = null;
	// OutputStream out = null;
	// try {
	// File localFile = new
	// File(localFilePath);
	//
	// String fileName = localFile.getName();
	// SmbFile remoteFile = new SmbFile(remoteUrl + "/" + fileName);
	// in = new BufferedInputStream(new
	// FileInputStream(localFile));
	// out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
	// int length = (int) localFile.length();
	// // get the file length
	// byte buffer[] = new byte[length];
	// while (in.read(buffer) != -1) {
	// out.write(buffer);
	// }
	// } catch (Exception e) {
	// logger.error("There is a exception in this method:", e);
	// } finally {
	// try {
	// out.close();
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

}
