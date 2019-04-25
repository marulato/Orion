package org.orion.common.document.pdf;

import java.util.Map;

public class MessageUtil {


    public static String getTemplateMessage(String templateName, Map<String, Object> params) throws Exception {
        return TemplateMessageProvider.INSTANCE.getTemplateMessage(templateName, params);
    }

}


