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

    private final String ID_TAG             = "ModuelMappings";
    private final String MODUEL_ID_TAG      = "ModuelId";
    private final String URL_TAG            = "URL";
    private final String MODUEL_NAME_TAG    = "ModuelName";
    private final String DESC_TAG           = "Desc";
    private final String FUNCTION_TAG       = "Function";
    private final String FUNC_ID_TAG        = "FuncId";
    private final String FUNC_NAME_TAG      = "FuncName";
    private final String IS_SYSTEM_TAG      = "IsSystem";
    private final String LEVEL_TAG          = "Level";
    private final String REMARKS_TAG        = "Remarks";
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
                    String moduelName = null;
                    String desc = null;
                    String remarks = null;
                    for (Element mappingTag : mappingDetails) {
                        if (MODUEL_ID_TAG.equals(mappingTag.getName())) {
                            moduelId = dom4JReader.getElementValue(mappingTag);
                            if (moduelUrlMap.get(moduelId) == null) {
                                moduelUrlMap.put(moduelId, new ArrayList<>());
                            }
                        }
                        else if(MODUEL_NAME_TAG.equals(mappingTag.getName()))
                            moduelName = dom4JReader.getElementValue(mappingTag);
                        else if(DESC_TAG.equals(mappingTag.getName()))
                            desc = dom4JReader.getElementValue(mappingTag);
                        else if(REMARKS_TAG.equals(mappingTag.getName()))
                            remarks = dom4JReader.getElementValue(mappingTag);
                    }

                    for (Element func : mappingDetails) {
                        if (FUNCTION_TAG.equals(func.getName())) {
                            ModuelUrlAssign moduelUrlAssign = new ModuelUrlAssign();
                            moduelUrlMap.get(moduelId).add(moduelUrlAssign);
                            moduelUrlAssign.setModuelId(moduelId);
                            moduelUrlAssign.setModuelName(moduelName);
                            moduelUrlAssign.setModuelDesc(desc);
                            moduelUrlAssign.setRemarks(remarks);
                            List<Element> functionTags = dom4JReader.getAllElementsUnderSpecified(func);
                            for (Element funcTag : functionTags) {
                                if (FUNC_ID_TAG.equals(funcTag.getName()))
                                    moduelUrlAssign.setFuncId(dom4JReader.getElementValue(funcTag));
                                else if(FUNC_NAME_TAG.equals(funcTag.getName()))
                                    moduelUrlAssign.setFuncName(dom4JReader.getElementValue(funcTag));
                                else if(URL_TAG.equals(funcTag.getName()))
                                    moduelUrlAssign.setUrl(dom4JReader.getElementValue(funcTag));
                                else if (LEVEL_TAG.equals(funcTag.getName()))
                                    moduelUrlAssign.setLevel(dom4JReader.getElementValue(funcTag));
                                else if(IS_SYSTEM_TAG.equals(funcTag.getName()))
                                    moduelUrlAssign.setIsSystem(dom4JReader.getElementValue(funcTag));
                            }
                        } else {
                            continue;
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
