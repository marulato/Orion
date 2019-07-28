package org.orion.general.backup.service;

import org.orion.common.miscutil.*;
import org.orion.general.backup.entity.GalleryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileBackupService {

    private final Logger logger = LoggerFactory.getLogger(FileBackupService.class);

    public List<GalleryEntity> getPicturesFromFtp(String remoteDir) throws Exception {
        List<GalleryEntity> galleryList = new ArrayList<>();
        FTPManager ftp = new FTPManager();
        try {
            String[] fileNames = ftp.listFilesPath(remoteDir);
            if (!ArrayUtil.isEmpty(fileNames)) {
                for (String fileName : fileNames) {
                    byte[] data = ftp.download(fileName);
                    if (!ArrayUtil.isEmpty(data)) {
                        GalleryEntity gallery = new GalleryEntity();
                        gallery.setFileName(StringUtil.getFileNameFromPath(fileName));
                        gallery.setSize(data.length);
                        gallery.setContent(data);
                        gallery.setAudit("TEST", DateUtil.now());
                        gallery.setHash(Encrtption.sha256(data).toUpperCase());
                        galleryList.add(gallery);
                    }
                }
            }
        } catch (Exception e) {
            galleryList.clear();
            logger.error("", e);
        } finally {
            ftp.close();
        }
        return galleryList;
    }
}
