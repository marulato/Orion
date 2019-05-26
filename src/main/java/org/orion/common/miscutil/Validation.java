package org.orion.common.miscutil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orion.common.mastercode.ErrorCode;
import org.orion.common.validation.ValidateWithMethod;
import org.orion.systemAdmin.service.MasterCodeService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Validation {

    private static List<Map<Field, Method[]>> fieldMehtodList;
    private static List<Map<Field, String[]>> fieldErrorList;
    private static Field[] fields;
    private static Object target;
    private static final Logger logger = LogManager.getLogger(Validation.class);
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
                if (fieldMehtodList != null && fieldMehtodList.size() > 0) {
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

    public static List<ErrorCode> doValidate(Object target) {
        List<String> errorCodeList = validateWithCode(target);
        if (errorCodeList != null && !errorCodeList.isEmpty()) {
            MasterCodeService masterCodeService = SpringUtil.getBean(MasterCodeService.class);
            return masterCodeService.getErrorCodeList(errorCodeList);
        }
        return null;
    }
}
