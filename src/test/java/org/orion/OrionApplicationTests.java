package org.orion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.orion.common.dao.DaoGenerateSvc;
import org.orion.common.miscutil.Encrtption;
import org.orion.systemAdmin.service.PrivilegeAssignService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrionApplicationTests {

    @Resource
    private DaoGenerateSvc daoGenerateSvc;
    @Resource
    PrivilegeAssignService privilegeAssignService;

    @Test
    public void test() throws Exception {
       String s =Encrtption.encryptPassword("123456");
        System.out.println(s);
    }

}
