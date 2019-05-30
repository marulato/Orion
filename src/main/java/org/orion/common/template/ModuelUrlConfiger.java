package org.orion.common.template;

import org.dom4j.Element;
import org.orion.common.document.xml.Dom4JReader;
import org.orion.common.miscutil.StringUtil;
import org.orion.systemAdmin.entity.ModuelUrlAssign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuelUrlConfiger {

    private final String ID_TAG         = "ModuelMappings";
    private final String URL_TAG        = "Url";
    private final String DESC_TAG       = "Desc";
    private final String FUNC_TAG       = "Func";
    private final String LEVEL_TAG      = "Level";
    private final String REMARKS_TAG    = "Remarks";
    private final Logger logger = LoggerFactory.getLogger(ModuelUrlConfiger.class);
    private Map<String, List<ModuelUrlAssign>> moduelUrlMap;

    public void loadConfig(String configPath) {
        if (!StringUtil.isEmpty(configPath) && "xml".equalsIgnoreCase(StringUtil.getFileSuffix(configPath))) {
            moduelUrlMap = new HashMap<>();
            try {
                Dom4JReader dom4JReader = new Dom4JReader(configPath);
                Element mappingRoot = dom4JReader.getRootElement();
                List<Element> urlMappingList = dom4JReader.getAllElementsUnderSpecified(mappingRoot);
                for (Element mapping : urlMappingList) {
                    List<Element> mappingDetails = dom4JReader.getAllElementsUnderSpecified(mapping);
                    String moduelId = null;
                    for (Element subTag : mappingDetails) {
                        ModuelUrlAssign moduelUrlAssign = new ModuelUrlAssign();
                        if (ID_TAG.equals(subTag.getName())) {
                            moduelId = dom4JReader.getElementValue(subTag);
                            moduelUrlAssign.setModuelId(moduelId);
                            if (moduelUrlMap.get(moduelId) == null)
                                moduelUrlMap.put(moduelId, new ArrayList<>());
                            else
                                moduelUrlMap.get(moduelId).add(moduelUrlAssign);
                        } else if (URL_TAG.equals(subTag.getName())) {
                            moduelUrlAssign.setUrl(dom4JReader.getElementValue(subTag));
                        } else if(DESC_TAG.equals(subTag.getName())) {
                            moduelUrlAssign.setModuelDesc(dom4JReader.getElementValue(subTag));
                        } else if(FUNC_TAG.equals(subTag.getName())) {
                            moduelUrlAssign.setFuncDesc(dom4JReader.getElementValue(subTag));
                        } else if(LEVEL_TAG.equals(subTag.getName())) {
                            moduelUrlAssign.setLevel(dom4JReader.getElementValue(subTag));
                        } else if(REMARKS_TAG.equals(subTag.getName())) {
                            moduelUrlAssign.setRemarks(dom4JReader.getElementValue(subTag));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error Initializing Moduel-Url Assignment Configuration, Please check the xml file", e);
            }
        }

    }

    public Map<String, List<ModuelUrlAssign>> getModuelUrlMap() {
        return moduelUrlMap;
    }
}
