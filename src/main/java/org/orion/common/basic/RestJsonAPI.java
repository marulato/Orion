package org.orion.common.basic;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.Serializable;

public class RestJsonAPI implements Serializable  {

    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private Object apiReturn;
    private ServerResponseMonitor responseMonitor;

    public RestJsonAPI(Object apiReturn, Boolean isArray) {
        this.apiReturn = apiReturn;
        if(apiReturn != null) {
            if (isArray == null || !isArray)
                jsonObject = JSONObject.fromObject(apiReturn);
            else
                jsonArray = JSONArray.fromObject(apiReturn);
        }
    }

    public void setResponsePerfomance() {

    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public Object getApiReturn() {
        return apiReturn;
    }

    public ServerResponseMonitor getResponseMonitor() {
        return responseMonitor;
    }
}
