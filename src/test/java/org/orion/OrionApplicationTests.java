package org.orion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.orion.common.dao.DaoGenerateSvc;
import org.orion.common.scheduel.JobScheduelManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrionApplicationTests {

    @Resource
    private DaoGenerateSvc daoGenerateSvc;
    @Resource
    JobScheduelManager jobScheduelManager;

    @Test
    public void test() throws Exception {
        String s = daoGenerateSvc.createUpdateSql("USER_TBL", "");
        System.out.println(s);
    }

}
