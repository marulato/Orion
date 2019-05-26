package org.orion.common.miscutil;

import org.orion.common.basic.JSONMessage;
import org.orion.common.mastercode.ErrorCode;

public class MessageUtil {

    public static JSONMessage getMessage(ErrorCode errorCode, Exception e, String message) {
        JSONMessage jsonMessage = new JSONMessage();
        StringBuilder builder = new StringBuilder();
        jsonMessage.setLevel("E");
        if (errorCode != null) {
            jsonMessage.setLevel("D");
            jsonMessage.setErrorCode(errorCode);
            jsonMessage.setCode(errorCode.getErrorCode());
            builder.append("Application Verification Error: [").append(jsonMessage.getCode()).append("]");
            builder.append(" ").append(errorCode.getErrorDesc());
        }
        if (e != null && e.getStackTrace() != null) {
            jsonMessage.setException(e);
            jsonMessage.setLevel("A");
            builder.append(" nested exception is ").append(e.getMessage());
        }
        if (!StringUtil.isEmpty(message)) {
            builder.append(" ").append(message);
        }
        jsonMessage.setMessage(builder.toString());
        return jsonMessage;
    }
}
