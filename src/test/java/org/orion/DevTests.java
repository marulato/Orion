package org.orion;

import net.sf.json.JSONObject;
import org.dom4j.Element;
import org.junit.Test;
import org.orion.common.dao.BaseSQLProvider;
import org.orion.common.document.xml.Dom4JReader;
import org.orion.common.miscutil.Config;
import org.orion.systemAdmin.entity.ModuelUrlAssign;
import org.orion.systemAdmin.entity.User;
import org.orion.systemAdmin.service.PrivilegeAssignService;

import java.util.ArrayList;
import java.util.List;

public class DevTests {
    @Test
    public void test() throws Exception {
        PrivilegeAssignService service = new PrivilegeAssignService();
        List<ModuelUrlAssign> list = service.initModuelUrlAssign("D:\\orion_moduel_url.xml");
        for (ModuelUrlAssign m : list) {
            System.out.println(m);
        }
    }
}
