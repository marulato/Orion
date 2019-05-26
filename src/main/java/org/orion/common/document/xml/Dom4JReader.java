package org.orion.common.document.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.orion.common.miscutil.IOUtil;
import org.orion.common.miscutil.StringUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dom4JReader {
    private Document document;

    public Dom4JReader(String filePath) throws DocumentException {
        if ("xml".equalsIgnoreCase(StringUtil.getFileSuffix(StringUtil.getFileNameFromPath(filePath)))) {
            document = read(IOUtil.getFileInputStream(filePath));
        }
    }

    public Map<String, String> getContentEntry(String tagName) {
        Map<String, String> tagAndValue = new HashMap<>();
        Element rootElement = getRootElement();
        Element currentTag = getElementByName(rootElement, tagName);
        if (currentTag != null) {
            List<Element> subTags = getAllElementsUnderSpecified(currentTag);
            if (subTags != null && !subTags.isEmpty()) {
                subTags.forEach((tag) -> {
                    tagAndValue.put(tag.getName(), tag.getTextTrim());
                });
            }
        }
        return tagAndValue;
    }

    private Document read(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(inputStream);
    }

    public Element getRootElement() {

        return document.getRootElement();
    }


    public Element getElementByName(Element father, String name) {
        if (father == null || StringUtil.isEmpty(name)) {
            return null;
        }
        return father.element(name);
    }

    public List<Element> getAllElementsUnderSpecified(Element father) {
        if (father == null) {
            return null;
        }
        List<Element> elements = father.elements();
        return elements;
    }

    public String getElementValue(Element element) {
        if (element == null) {
            return null;
        }
        return element.getText();
    }

    public Attribute getAttribute(Element element, String attrName) {
        if (element == null) {
            return null;
        }
        return element.attribute(attrName);
    }

    public List<Attribute> getAllAttrsOfElement(Element element) {
        if (element == null) {
            return null;
        }
        return element.attributes();
    }

    private String getAttrValue(Attribute attr) {
        if (attr == null) {
            return null;
        }
        return attr.getText();
    }

    public String getAttrValue(Element element, String attrName) {
        if (element == null) {
            return null;
        }
        return getAttrValue(getAttribute(element, attrName));

    }

    public List<String> getAllAttrValues(Element element) {
        if (element == null) {
            return null;
        }
        List<Attribute> attributes = getAllAttrsOfElement(element);
        List<String> attrValues = null;
        if (attributes != null) {
            attrValues = new ArrayList<String>();
            for (Attribute attribute : attributes) {
                attrValues.add(getAttrValue(attribute));
            }
        }
        return attrValues;
    }
}
