/** 
 *	[:LC:]
 *
 *  PdfUtil.java created on May 3, 2018
 *
 * Copyright 2003-2017, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 * No part of this material may be copied, reproduced, transmitted,
 * stored in a retrieval system, reverse engineered, decompiled,
 * disassembled, localised, ported, adapted, varied, modified, reused,
 * customised or translated into any language in any form or by any means,
 * electronic, mechanical, photocopying, recording or otherwise,
 * without the prior written permission of Ecquaria Technologies Pte Ltd.
 */
package org.orion.common.miscutil;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * PdfUtil
 *
 * @author Chen Lei
 */
public class PdfUtil {

	public static ElementList parseToElementList(String html, String css) throws IOException {
		// CSS
		CSSResolver cssResolver = new StyleAttrCSSResolver();
		if (css != null) {
			CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes("UTF-8")));
			cssResolver.addCss(cssFile);
		}

		// HTML
		CssAppliers cssAppliers = new CssAppliersImpl(new XMLWorkerFontProvider() {
			public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {
				if (StringUtil.isEmpty(fontName)) {
					fontName = FontFactory.TIMES_ROMAN;
				}
				return FontFactory.getFont(fontName, encoding, embedded, size, style, color);
			}
		});
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		htmlContext.setAcceptUnknown(true);
		htmlContext.autoBookmark(true);

		// Pipelines
		ElementList elements = new ElementList();
		ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
		CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

		// XML Worker
		XMLWorker worker = new XMLWorker(cssPipeline, true);
		XMLParser p = new XMLParser(worker, Charset.forName("UTF-8"));
		p.parse(new ByteArrayInputStream(html.getBytes("UTF-8")));

		return elements;
	}

}
