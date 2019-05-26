package org.orion.common.template;

import org.dom4j.Element;
import org.orion.common.document.xml.Dom4JReader;
import org.orion.common.miscutil.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleAssignConfiger {

    private final String ROOT_TAG   = "RoleAssignment";
    private final String ROLE_TAG   = "Role";
    private final String ID_TAG     = "Id";
    private final String MODUEL_TAG = "Moduel";
    private Map<String, List<String>> roleAssignMap;
    private final Logger logger = LoggerFactory.getLogger(RoleAssignConfiger.class);

    public final void loadConfig(String configPath) {
        if (!StringUtil.isEmpty(configPath) && "xml".equalsIgnoreCase(StringUtil.getFileSuffix(configPath))) {
            roleAssignMap = new HashMap<>();
            try {
                Dom4JReader dom4JReader = new Dom4JReader(configPath);
                Element roleRoot = dom4JReader.getRootElement();
                List<Element> roleTags = dom4JReader.getAllElementsUnderSpecified(roleRoot);
                for (Element roleTag : roleTags) {
                    List<Element> subTags = dom4JReader.getAllElementsUnderSpecified(roleTag);
                    String roleId = null;
                    for (Element subTag : subTags) {
                        if (ID_TAG.equals(subTag.getName())) {
                            roleAssignMap.put(dom4JReader.getElementValue(subTag), new ArrayList<>());
                            roleId = dom4JReader.getElementValue(subTag);
                        } else {
                            roleAssignMap.get(roleId).add(dom4JReader.getElementValue(subTag));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error Initializing Role Assignment Configuration, Please check the xml file", e);
            }
        }
    }

    public Map<String, List<String>> getRoleAssignMap() {
        return roleAssignMap;
    }
}
