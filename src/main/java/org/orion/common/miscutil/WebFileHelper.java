package org.orion.common.miscutil;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class WebFileHelper {

    public static void upload(String path, MultipartFile file, String fileName) throws Exception {
        if (file != null && !file.isEmpty() && !StringUtil.isEmpty(path)) {
            File dest = new File(new File(path).getAbsolutePath()+ "/" + fileName);
            file.transferTo(dest);
        }
    }

    public static void download(String from, String fileName, HttpServletResponse response) throws Exception {
        if (!StringUtil.isEmpty(from) && response != null) {
            BufferedInputStream bufferdIn = null;
            OutputStream output = null;
            try{
                File file = new File(from, fileName);
                response.addHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                bufferdIn = new BufferedInputStream(new FileInputStream(file));
                output = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = bufferdIn.read(buffer)) != -1) {
                    output.write(buffer, 0 ,len);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                bufferdIn.close();
                output.close();
            }

        }

    }

    public static void download(byte[] data, String fileName, HttpServletResponse response) throws Exception {
        if (response != null && data != null && !StringUtil.isEmpty(fileName)) {
            OutputStream output = null;
            try{
                response.addHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                output = response.getOutputStream();
                output.write(data);
            } catch (Exception e) {
                throw e;
            } finally {
                output.close();
            }

        }
    }
}
