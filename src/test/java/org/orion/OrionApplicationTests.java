package org.orion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.orion.common.dao.DaoGenerateSvc;
import org.orion.common.scheduel.JobScheduelManager;
import org.orion.systemAdmin.batchjob.TestBatchJob3;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrionApplicationTests {

    @Resource
    private DaoGenerateSvc daoGenerateSvc;
    @Resource
    JobScheduelManager jobScheduelManager;

    @Test
    public void test() throws Exception {
        Class jobClass = TestBatchJob3.class;
        Method method = jobClass.getSuperclass().getDeclaredMethod("start");
        method.setAccessible(true);
        method.invoke(jobClass.getConstructor(String.class).newInstance("good"));
    }

}
