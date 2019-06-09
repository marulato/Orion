package org.orion.common.template;

import org.dom4j.Element;
import org.orion.common.document.xml.Dom4JReader;
import org.orion.common.miscutil.StringUtil;
import org.orion.systemAdmin.entity.RoleModuelAssign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleAssignConfiger {

    private final String ROOT_TAG   = "RoleAssignment";
    private final String ROLE_ID_TAG   = "RoleId";
    private final String FUNC_ID_TAG     = "FuncId";
    private final String MODUEL_TAG = "Moduel";
    private Map<String, List<RoleModuelAssign>> roleAssignMap;
    private final Logger logger = LoggerFactory.getLogger(RoleAssignConfiger.class);

    public final void loadConfig(String configPath) {
        if (!StringUtil.isEmpty(configPath) && "xml".equalsIgnoreCase(StringUtil.getFileSuffix(configPath))) {
            roleAssignMap = new HashMap<>();
            try {
                Dom4JReader dom4JReader = new Dom4JReader(configPath);
                Element roleRoot = dom4JReader.getRootElement();
                //RoleAssignment
                List<Element> roleAssigns = dom4JReader.getAllElementsUnderSpecified(roleRoot);
                for (Element roleTag : roleAssigns) {
                    //Role
                    List<Element> subTags = dom4JReader.getAllElementsUnderSpecified(roleTag);
                    String roleId = null;
                    for (Element subTag : subTags) {
                        if (ROLE_ID_TAG.equals(subTag.getName())) {
                            roleId = dom4JReader.getElementValue(subTag);
                            if (roleAssignMap.get(roleId) == null)
                                roleAssignMap.put(roleId, new ArrayList<>());
                        }
                    }
                    for (Element moduelTag : subTags) {
                        if (MODUEL_TAG.equals(moduelTag.getName())) {
                            String moduelId = dom4JReader.getAttrValue(moduelTag, "id");
                            List<Element> funcTags = dom4JReader.getAllElementsUnderSpecified(moduelTag);
                            for (Element funcTag : funcTags) {
                                RoleModuelAssign roleModuelAssign = new RoleModuelAssign();
                                roleModuelAssign.setRoleId(roleId);
                                roleModuelAssign.setModuelId(moduelId);
                                roleModuelAssign.setFuncId(dom4JReader.getElementValue(funcTag));
                                roleModuelAssign.setPermission(dom4JReader.getAttrValue(funcTag, "permission"));
                                roleAssignMap.get(roleId).add(roleModuelAssign);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error Initializing Role-Moduel Assignment Configuration, Please check the xml file", e);
            }
        }
    }

    public Map<String, List<RoleModuelAssign>> getRoleAssignMap() {
        return roleAssignMap;
    }
}
