package org.orion;

import net.sf.json.JSONObject;
import org.dom4j.Element;
import org.junit.Test;
import org.orion.common.dao.BaseSQLProvider;
import org.orion.common.document.xml.Dom4JReader;
import org.orion.common.miscutil.Config;
import org.orion.systemAdmin.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DevTests {
    @Test
    public void test() throws Exception {
        Dom4JReader reader = new Dom4JReader("D:\\test.xml");
        Element root = reader.getRootElement();
        List<Element> elements = reader.getAllElementsUnderSpecified(root);
        for (Element e: elements) {
            List<Element> elements2 = reader.getAllElementsUnderSpecified(e);
            for (Element e2 : elements2) {
                System.out.println(reader.getElementValue(e2));
            }
        }
    }
}
