package org.orion;

import org.junit.Test;
import org.orion.systemAdmin.batchjob.TestBatchJob3;

import java.lang.reflect.Method;

public class DevTests {
    @Test
    public void test() throws Exception {
        Class jobClass = TestBatchJob3.class;
        Method method = jobClass.getSuperclass().getDeclaredMethod("start");
        method.setAccessible(true);
        method.invoke(jobClass.getConstructor(String.class).newInstance("good"));
    }
}
