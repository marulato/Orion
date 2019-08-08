package org.orion.common.basic;

import org.orion.common.miscutil.StringUtil;

public class Response {

    private String code;
    private Object object;
    private String msg;
    private long timeCost;
    private Object errors;

    public void setObject(Object object, boolean escape) throws Exception {
        if (escape) {
            StringUtil.escapeInjection(object);
        }
        this.object = object;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) throws Exception {
        setObject(object, true);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(long timeCost) {
        this.timeCost = timeCost;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}
