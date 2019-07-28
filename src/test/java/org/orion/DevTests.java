package org.orion;

import org.junit.Test;
import org.orion.common.miscutil.ArrayUtil;

public class DevTests {
    @Test
    public void test() throws Exception {
        String [] strings = {"a", "b", "c", "1", "2", "3"};
        System.out.println(ArrayUtil.contains(strings, "1", "a", "r"));
    }

    @Test
    public void test2() throws Exception {

    }
}
