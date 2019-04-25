/*
 * BaseGenerator.java created on Jul 22, 2012 at 8:45:24 PM
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

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;


/**
 * IGenerator
 *
 * @author Veerappan
 */
public abstract class BaseGenerator {
    protected String stamp;
    protected String pageHeader;
    protected String pageFooter;
    protected boolean needPageNo;

    public byte[] generate(String html) throws Exception {
        return generate(html, null);
    }
    public byte[] generate(String html, String css) throws Exception {
        return generate(html, css, new Document(PageSize.A4, 60, 60, 50, 50));
    }
    public abstract byte[] generate(String html, String css, Document doc) throws Exception;

    
    public String getStamp() {
        return stamp;
    }
    public void setStamp(String stamp) {
        this.stamp = stamp;
    }
    public String getPageHeader() {
        return pageHeader;
    }
    public void setPageHeader(String pageHeader) {
        this.pageHeader = pageHeader;
    }
    public String getPageFooter() {
        return pageFooter;
    }
    public void setPageFooter(String pageFooter) {
        this.pageFooter = pageFooter;
    }
    public boolean isNeedPageNo() {
        return needPageNo;
    }
    public void setNeedPageNo(boolean needPageNo) {
        this.needPageNo = needPageNo;
    }
}
