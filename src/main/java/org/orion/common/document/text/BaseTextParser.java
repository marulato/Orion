package org.orion.common.document.text;

import org.orion.common.miscutil.FileUtil;
import org.orion.common.miscutil.IOUtil;
import org.orion.common.miscutil.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseTextParser {

    private List<String> contentCache;
    protected String filePath;
    private Text text;
    private final Logger logger = LoggerFactory.getLogger(BaseTextParser.class);

    public BaseTextParser(String path) {
        if (StringUtil.isBlank(path))
            return;
        filePath = path;
    }

    public final Text read() throws Exception {
        List<String> content = null;
        if (!StringUtil.isBlank(filePath)) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
                content = new ArrayList<>();
                String row = null;
                while ((row = reader.readLine()) != null) {
                    content.add(row);
                }
                exclude(content);
                if (contentCache == null)
                    contentCache = content;
                text = new Text();
                text.setContent(content);
                text.setFilePath(filePath);
                text.setSize(FileUtil.getSize(new File(filePath), "KB"));
            } catch (Exception e) {
                logger.error("Exceptions Occurred When Trying to Read Text", e);
                throw e;
            } finally {
                try {
                    IOUtil.close(reader);
                } catch (Exception e) {
                    logger.error("Failed to Close Input Stream", e);
                    throw e;
                }
            }
        }
        return text;
    }

    public void refresh() {
        try {
            read();
        } catch (Exception e) {
            logger.error("Failed to Refresh", e);
        } finally {
            if (text != null)
                contentCache = text.getContent();
        }
    }

    public void rollBack() throws Exception {
        if (contentCache != null) {
            clear();
            append(contentCache, true);
        }
    }

    public abstract Map<String, Integer> fieldMap();
    public abstract Map<Integer, List<String>> separate(Text text);
    public abstract void exclude(List<String> content);

    public final Text clear() throws Exception {
        return write('\0', false, true);
    }

    public final Text write(char ch, Boolean autoReflesh) throws Exception {
        return write(ch, autoReflesh, true);
    }

    public final Text append(char ch, Boolean autoReflesh) throws Exception {
        return write(ch, autoReflesh, false);
    }

    public final Text write (String line, Boolean autoReflesh) throws Exception {
        return write(line, autoReflesh, true);
    }

    public final Text append(String line, Boolean autoReflesh) throws Exception {
        return write(line, autoReflesh, false);
    }

    public final Text write(List<String> content, Boolean autoReflesh) throws Exception {
        return write(content, autoReflesh, true);
    }

    public final Text append(List<String> content, Boolean autoReflesh) throws Exception {
        return write(content, autoReflesh, false);
    }

    public final Text write(char ch, Boolean autoReflesh, boolean needClear) throws Exception {
        BufferedWriter writer = null;
        if (StringUtil.isBlank(filePath))
            return text;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            if (needClear)
                writer.write(ch);
            else
                writer.append(ch);
        } catch (Exception e) {
            logger.error("Exceptions Occurred When Trying to write into Text", e);
            throw e;
        } finally {
            try {
                IOUtil.close(writer);
                if (autoReflesh == null || autoReflesh == true)
                    refresh();
                else {
                    if (text == null)
                        text = new Text();
                    text.setFilePath(filePath);
                    text.setSize(FileUtil.getSize(new File(filePath), "KB"));
                    if (needClear)
                        text.setContent(Arrays.asList(String.valueOf(ch)));
                    else {
                        if (text.getContent() == null)
                            text.setContent(new ArrayList<>());
                        text.getContent().add(String.valueOf(ch));
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to Close Output Stream", e);
                throw e;
            }
        }
        return text;
    }

    public final Text write (String line, Boolean autoReflesh, boolean needClear) throws Exception {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            if (!StringUtil.isBlank(line)) {
                if (needClear)
                    writer.write(line);
                else
                    writer.append(line);
            }
        } catch (Exception e) {
            logger.error("Exceptions Occurred When Trying to write into Text", e);
            throw e;
        } finally {
            try {
                IOUtil.close(writer);
                if (autoReflesh == null || autoReflesh == true)
                    refresh();
                else {
                    if (text == null)
                        text = new Text();
                    text.setFilePath(filePath);
                    text.setSize(FileUtil.getSize(new File(filePath), "KB"));
                    if (needClear)
                        text.setContent(Arrays.asList(line));
                    else {
                        if (text.getContent() == null)
                            text.setContent(new ArrayList<>());
                        text.getContent().add(line);
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to Close Output Stream", e);
                throw e;
            }
        }
        return text;
    }

    public final Text write (List<String> content, Boolean autoReflesh, boolean needClear) throws Exception {
        BufferedWriter writer = null;
        try {
             writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
             if (needClear)
                 writer.write('\0');
            if (content != null && content.size() > 0) {
                for (String  line : content) {
                    writer.append(line);
                    writer.append("\r\n");
                }
            }
        } catch (Exception e) {
            logger.error("Exceptions Occurred When Trying to write into Text", e);
            throw e;
        } finally {
            try {
                IOUtil.close(writer);
                if (autoReflesh == null || autoReflesh == true)
                    refresh();
                else {
                    if (text == null)
                        text = new Text();
                    text.setFilePath(filePath);
                    text.setSize(FileUtil.getSize(new File(filePath), "KB"));
                    if (needClear)
                        text.setContent(content);
                    else {
                        if (text.getContent() == null)
                            text.setContent(new ArrayList<>());
                        text.getContent().addAll(content);
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to Close Output Stream", e);
                throw e;
            }
        }
        return text;
    }

    public <T> List<T> generateMapping(Class<T> entity) {
        if (text == null)
            refresh();
        List<T> entityList = null;
        try {
            if (entity != null) {
                Map<String, Integer> fieldMap = fieldMap();
                if (fieldMap != null && fieldMap.size() > 0) {
                    Field[] fields = entity.getDeclaredFields();
                    Method[] methods = entity.getDeclaredMethods();
                    List<String> lines = text.getContent();
                    if (lines != null && lines.size() > 0) {
                        entityList = new ArrayList<>();
                        for (int row = 0; row < lines.size(); row ++) {
                            T target = entity.getConstructor().newInstance();
                            Set<String> keySet = fieldMap.keySet();
                            for(String fieldName : keySet) {
                                String setterStr = "set" + StringUtil.toUpperCaseByIndex(fieldName, 0);
                                try {
                                    Method setter = entity.getDeclaredMethod(setterStr, String.class);
                                    setter.setAccessible(true);
                                    setter.invoke(target, separate(text).get(row).get(fieldMap.get(fieldName)));
                                } catch (NoSuchMethodException e) {
                                    Field field =entity.getDeclaredField(fieldName);
                                    field.setAccessible(true);
                                    field.set(target, separate(text).get(row).get(fieldMap.get(fieldName)));
                                }
                            }
                            entityList.add(target);
                        }
                    }
                }
            }
        } catch (Exception e) {
            entityList.clear();
        }

        return entityList;
    }

}
