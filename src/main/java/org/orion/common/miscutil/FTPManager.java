package org.orion.common.miscutil;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FTPManager {

    private FTPClient ftpClient;
    private String host;
    private String username;
    private String password;
    private String port;
    private final Logger logger = LoggerFactory.getLogger("FTP Manager");

    public FTPManager() {
        init();
        connect();
    }

    public boolean isDirExists(String directoryPath) {
        if (ftpClient == null) {
            return false;
        }
        try {
            ftpClient.changeWorkingDirectory(directoryPath);
            if (ftpClient.getReplyCode() == 250)
                return true;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public boolean download(String ftpFilePath, String localFilePath) {
        if (ftpClient == null) {
            return false;
        }
        boolean isSuccess = false;
        ftpFilePath = ftpFilePath.replaceAll("\\\\", "/");
        localFilePath = localFilePath.replaceAll("\\\\", "/");
        try {
            FileOutputStream outputStream = new FileOutputStream(localFilePath);
            isSuccess = ftpClient.retrieveFile(ftpFilePath, outputStream);
            outputStream.close();
            if (!isSuccess) {
                FileUtil.deleteFile(localFilePath);
                logger.warn("The file: " + ftpFilePath + " doesn't exists on ftp server");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return isSuccess;
    }

    public byte[] download(String ftpFilePath) {
        if (ftpClient == null) {
            return null;
        }
        ftpFilePath = ftpFilePath.replaceAll("\\\\", "/");
        byte[] data = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ftpClient.retrieveFile(ftpFilePath, outputStream);
            data = outputStream.toByteArray();
            outputStream.close();
        } catch (Exception e) {
            logger.error("", e);
        }
        return data;
    }

    public boolean download(String ftpDir, String fileName, String localDir, String localFileName) {
        String ftpFilePath = StringUtil.appendFileName(ftpDir, fileName);
        String localFilePath = StringUtil.appendFileName(localDir, localFileName);
        return download(ftpFilePath, localFilePath);
    }

    public boolean upload(String ftpDir, String localFilePath) throws IOException {
        if (ftpClient == null) {
            return false;
        }
        boolean isSuccess = false;
        ftpDir = ftpDir.replaceAll("\\\\", "/");
        File localFile = new File(localFilePath);
        if (localFile.exists() && localFile.isFile()) {
            FileInputStream inputStream = new FileInputStream(localFile);
            if (!isDirExists(ftpDir)) {
                mkdirs(ftpDir);
            }
            String name = StringUtil.getFileNameFromPath(localFilePath);
            isSuccess = ftpClient.storeFile(StringUtil.appendFileName(ftpDir, name), inputStream);
            inputStream.close();
        }
        return isSuccess;
    }

    public void mkdirs(String ftpDir) throws IOException {
        if (ftpClient == null) {
            return;
        }
        if (!StringUtil.isEmpty(ftpDir)) {
            ftpDir = ftpDir.replaceAll("\\\\", "/");
            if (isDirExists(ftpDir)) {
                return;
            } else {
                if (ftpDir.startsWith("/")) {
                    StringUtil.deleteCharAt(ftpDir, 0);
                }
                String[] dirs = ftpDir.split("/");
                if (dirs != null && dirs.length > 0) {
                    StringBuilder path = new StringBuilder();
                    for(String dir : dirs) {
                        path.append("/").append(dir);
                        if (isDirExists(path.toString())) {
                            continue;
                        } else {
                            ftpClient.makeDirectory(path.toString());
                        }
                    }
                }
            }
        }
    }

    public String[] listFilesPath(String directoryPath) throws Exception {
        if (ftpClient == null) {
            return null;
        }
        if (isDirExists(directoryPath)) {
            return ftpClient.listNames(directoryPath);
        }
        return null;
    }

    private void init() {
        host = Config.get("orion.ftp.host");
        username = Config.get("orion.ftp.username");
        password = Config.get("orion.ftp.password");
        port = Config.get("orion.ftp.port");
    }

    private void connect() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        try {
            ftpClient.connect(host, Integer.parseInt(port));
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            int replyCode = ftpClient.getReplyCode();
            if (FTPReply.isPositiveCompletion(replyCode)) {
                logger.info("Connect to " + host + " successfully");
            } else {
                logger.info("Connect to " + host + " FAILED");
            }
        } catch (Exception e) {
            ftpClient = null;
            logger.error("Connecting to FTP server encountered an exception", e);
        }
    }

    public void close() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.error("", e);
            } finally {
                ftpClient = null;
            }
        }
    }
}
