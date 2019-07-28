package org.orion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.orion.common.dao.crud.CrudManager;
import org.orion.common.miscutil.StringUtil;
import org.orion.general.backup.entity.GalleryEntity;
import org.orion.general.backup.service.FileBackupService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrionApplicationTests {

    @Resource
    private CrudManager crudManager;
    @Resource
    private FileBackupService backupService;

    @Test
    public void test() throws Exception {
        List<GalleryEntity> galleryEntities = backupService.getPicturesFromFtp("/tieba");
        for (GalleryEntity gallery : galleryEntities) {
            gallery.setCategory("球裤运动裤");
            gallery.setName(StringUtil.getFileNameFromPath(gallery.getFileName()));
            gallery.setDescription("运动裤勃起");
            crudManager.create(gallery);
        }
    }

}
