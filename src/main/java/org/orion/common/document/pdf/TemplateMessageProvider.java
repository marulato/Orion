/*
 * TemplateMessageProvider.java created on Jul 9, 2012 at 12:35:29 PM
 *
 * Copyright 2003-2012, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 * No part of this material may be copied, reproduced, transmitted,
 * stored in a retrieval system, reverse engineered, decompiled,
 * disassembled, localised, ported, adapted, varied, modified, reused,
 * customised or translated into any language in any form or by any means,
 * electronic, mechanical, photocopying, recording or otherwise,
 * without the prior written permission of Ecquaria Technologies Pte Ltd.
 */
package org.orion.common.document.pdf;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orion.common.miscutil.StringUtil;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * TemplateMessageProvider
 *
 * @author Veerappan
 */
public class TemplateMessageProvider {
    private static final Logger logger = LogManager.getLogger(TemplateMessageProvider.class);

    public static final TemplateMessageProvider INSTANCE = new TemplateMessageProvider();
    private String serverPath = TemplateMessageProvider.class.getResource("").toString().replaceAll("%20", " ").substring(5);
    private Configuration cfg;

    public String getTemplateMessage(String templateName, Map<String, Object> params) throws Exception {
        if (cfg == null) {
            throw new Exception("The Freemarker configuration has not been initialized.");
        }
        if (StringUtil.isEmpty(templateName)) {
            throw new Exception("The template name passed in is empty.");
        }

        if (params == null) {
            params = new HashMap<String, Object>();
        }

        StringWriter writer = new StringWriter();
        Template temp = cfg.getTemplate(templateName);
        temp.process(params, writer);
        writer.flush();
        return writer.toString();
    }

    public void init(String rootPath) {
        cfg = new Configuration();
        try {
        	//commented for local testing ..
        	logger.debug("===================serverPath Path====================" + serverPath);
        	
        	String path = serverPath + "ftl/";
        	if (System.getProperty("file.separator").equals("\\")) {
				logger.debug("generate filename under Windows..");
				path =  path.replaceAll("/", "\\\\\\\\");
			}else {
				logger.debug("generate filename under UNIX..");
				path = path.replaceAll("/","/");
				path="/"+path;
			}	
        	
        	logger.debug("===================project Path====================" + path);
        	cfg.setDirectoryForTemplateLoading(new File(path));
        	cfg.setDefaultEncoding("UTF-8");
        } catch (Exception ex) {
            logger.error("Failed to initialize Freemarker configuration.", ex);
        }
    }

    private TemplateMessageProvider() {init("/web");}

}
