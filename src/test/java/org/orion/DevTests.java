package org.orion;

import org.junit.Test;
import org.orion.common.document.xlsx.Excel;

import java.util.ArrayList;
import java.util.List;

public class DevTests {
    @Test
    public void test() throws Exception {
        Excel excel = new Excel("E:\\test2.xlsx", "Sheet1");
        List<String> content = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            content.add("大福射在内裤里" + i);
        }
        long start1 = System.currentTimeMillis();
        excel.setRow(1, content);
        excel.setRow(1, content, 15);
        long end1 = System.currentTimeMillis();
        System.out.println("生成耗时: " + (end1 -start1) + " ms");
        long start = System.currentTimeMillis();
        excel.save();
        long end = System.currentTimeMillis();
        System.out.println("保存耗时: " + (end - start) + " ms");

    }

    @Test
    public void test2() {
    }
}
