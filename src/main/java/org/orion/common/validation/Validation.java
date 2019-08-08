package org.orion.common.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orion.common.mastercode.ErrorCode;
import org.orion.common.message.DataManager;
import org.orion.common.miscutil.ReflectionUtil;
import org.orion.common.miscutil.StringUtil;
import org.orion.common.miscutil.ValidationUtil;
import org.orion.common.validation.annotation.Length;
import org.orion.common.validation.annotation.Regex;
import org.orion.common.validation.annotation.ValidateWithMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Validation {

    private static List<Map<Field, Method[]>> fieldMehtodList;
    private static List<Map<Field, String[]>> fieldErrorList;
    private static Field[] fields;
    private static Object target;
    private static boolean hasAnnotation;
    private static final Logger logger = LogManager.getLogger("Orion Validation Log");
    private static void initClass(Object object) {
        if (object != null) {
            target = object;
            fields = target.getClass().getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                }
            }
            fieldMehtodList = new ArrayList<Map<Field,Method[]>>();
            fieldErrorList = new ArrayList<Map<Field,String[]>>();
        }
    }

    private static void initValidationMap() {
        if (fields != null && fields.length > 0) {
            Method[] methods = target.getClass().getDeclaredMethods();
            Map<String, Method> methodNameMap = new HashMap<String, Method>();
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    method.setAccessible(true);
                    methodNameMap.put(method.getName(), method);
                }
            }
            for (Field field : fields) {
                if (field.isAnnotationPresent(ValidateWithMethod.class)) {
                    hasAnnotation = true;
                    ValidateWithMethod validate = field.getAnnotation(ValidateWithMethod.class);
                    String[] methodNames = validate.methodName();
                    String[] errorCodes = validate.errorCode();
                    if (methodNames.length != errorCodes.length) {
                        throw new IllegalArgumentException("Inconsistent number of methods and error codes");
                    }
                    Map<Field, Method[]> fieldMethodMap = new HashMap<Field, Method[]>();
                    Map<Field, String[]> fieldErrorMap = new HashMap<Field, String[]>();
                    fieldErrorMap.put(field, errorCodes);
                    Method[] fieldValidateMethods = new Method[methodNames.length];
                    int index = 0;
                    for (String name : methodNames) {
                        if (methodNameMap.get(name) != null) {
                            fieldValidateMethods[index ++] = methodNameMap.get(name);
                        }
                    }
                    fieldMethodMap.put(field, fieldValidateMethods);
                    fieldMehtodList.add(fieldMethodMap);
                    fieldErrorList.add(fieldErrorMap);
                }
            }

        }
    }

    private static List<String> validateWithCode(Object target) {
        boolean finalResult = true;
        List<String> errorCodeList = new ArrayList<String>();
        if (target != null) {
            try {
                initClass(target);
                initValidationMap();
                if (fieldMehtodList != null && fieldMehtodList.size() > 0 && hasAnnotation) {
                    int index = 0;
                    for (Map<Field, Method[]> map : fieldMehtodList) {
                        if (map.size() > 0) {
                            Map<Field, String[]> errorMap = fieldErrorList.get(index);
                            Set<Field> keySet = map.keySet();
                            for (Field key : keySet) {
                                String[] errCodes = errorMap.get(key);
                                Method[] methodOnField = map.get(key);
                                if (methodOnField != null && methodOnField.length > 0) {
                                    int errIdx = 0;
                                    for (Method method : methodOnField) {
                                        boolean result = (boolean) method.invoke(target, key.get(target));
                                        if (!result) {
                                            errorCodeList.add(errCodes[errIdx]);
                                        }
                                        finalResult = finalResult && result;
                                        errIdx++;
                                    }
                                }
                            }
                        }
                        index++;
                    }
                }
            } catch (Exception e) {
                logger.error("An Exception Occured During Validation", e);
            }
        }
        return errorCodeList;
    }

    private static List<String> validateLength(Object target) {
        List<Field> fields = ReflectionUtil.getAnnotationFields(target, Length.class);
        List<String> errors = new ArrayList<>();
        for(Field field : fields) {
            Length anno = field.getAnnotation(Length.class);
            int min = anno.min();
            int max = anno.max();
            if (min > max) {
                int temp = min;
                min = max;
                max = temp;
            }
            try {
                String value = ReflectionUtil.getString(target, field);
                if (min > 0 && max >= min) {
                    if (StringUtil.isEmpty(value) || value.length() < min || value.length() > max) {
                        errors.add(anno.errorCode());
                    }
                } else if (min == max && min == 0) {
                    if (!StringUtil.isEmpty(value)) {
                        errors.add(anno.errorCode());
                    }
                } else if (min ==0 && max > min) {
                    if (value != null && value.length() > max) {
                        errors.add(anno.errorCode());
                    }
                }
            } catch (Exception e) {
                logger.error("An Exception Occured During Validation", e);
            }
        }
        return errors;
    }

    private static List<String> validateRegex(Object target) {
        List<Field> fields = ReflectionUtil.getAnnotationFields(target, Regex.class);
        List<String> errors = new ArrayList<>();
        for(Field field : fields) {
            Regex anno = field.getAnnotation(Regex.class);
            try {
                String value = ReflectionUtil.getString(target, field);
                String pattern = anno.pattern();
                String regex = anno.regex();
                if ("matches".equalsIgnoreCase(pattern)) {
                    if (!ValidationUtil.matches(regex, value)) {
                        errors.add(anno.errorCode());
                    }
                } else if("lookingAt".equalsIgnoreCase(pattern)) {
                    if (!ValidationUtil.lookingAt(regex, value)) {
                        errors.add(anno.errorCode());
                    }
                } else if("find".equalsIgnoreCase(pattern)) {
                    if (!ValidationUtil.find(regex, value)) {
                        errors.add(anno.errorCode());
                    }
                }
            } catch (Exception e) {
                logger.error("An Exception Occured During Validation", e);
            }
        }
        return errors;
    }


    public static List<ErrorCode> doValidate(Object target) {
        List<String> errorCodeList = validateWithCode(target);
        List<String> stringErrors = validateLength(target);
        List<String> regexErrors = validateRegex(target);
        List<ErrorCode> errorCodes = new ArrayList<>();
        errorCodeList.addAll(stringErrors);
        errorCodeList.addAll(regexErrors);
        if (errorCodeList != null && !errorCodeList.isEmpty()) {
            for (String code : errorCodeList) {
                errorCodes.add(DataManager.getErrorCode(code));
            }
        }
        return errorCodes;
    }
}
