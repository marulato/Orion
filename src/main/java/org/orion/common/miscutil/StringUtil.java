package org.orion.common.miscutil;


import java.util.Arrays;

public final class StringUtil {

    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static String toUpperCaseByIndex(String value,int index) {
        if (!isEmpty(value)) {
            char ch = value.charAt(index);
            if(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
                ch = Character.toUpperCase(ch);
                StringBuilder sBuilder = new StringBuilder();
                sBuilder.append(value.substring(0, index)).append(ch).append(value.substring(index+1));
                return sBuilder.toString();
            }
        }
        return null;
    }

    public static String toLowerCaseByIndex(String value,int index) {
        if (!isEmpty(value)) {
            char ch = value.charAt(index);
            if(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
                ch = Character.toLowerCase(ch);
                StringBuilder sBuilder = new StringBuilder();
                sBuilder.append(value.substring(0, index)).append(ch).append(value.substring(index+1));
                return sBuilder.toString();
            }
        }
        return null;
    }

    public static boolean isNumeric(String value) {
        return isEmpty(value) ? false : value.matches("[0-9\\.]+");
    }

    public static boolean isAlpha(String value) {
        return isEmpty(value) ? false : value.matches("[a-zA-Z]+");
    }

    /**
     *
     * @param value
     * @return Upper the first letter and lower others
     */
    public static String toUpperInitial(String value) {
        return toUpperCaseByIndex(value.toLowerCase(), 0);
    }

    public static boolean hasUpperCase(String value) {
        return isEmpty(value) ? false : value.matches(".*[A-Z]+.*");
    }

    public static boolean hasDigit(String value) {
        return isEmpty(value) ? false : value.matches(".*\\d+.*");
    }

    public static boolean hasLowerCase(String value) {
        return isEmpty(value) ? false : value.matches(".*[a-z]+.*");
    }

    public static boolean hasAlpha(String value) {
        return isEmpty(value) ? false : value.matches(".*[a-zA-Z]+.*");
    }

    public static  boolean hasSpecialSign(String value) {
        return isEmpty(value) ? false : value.matches(".*[\\+\\-/,\\.%\\^;:\\'\\[\\]\\{\\}\\?\\!\\*\\(\\)\\<\\>\\\\]+.*");
    }

    public static boolean hasAppointedChar(String value,char charAppointed) {
        return isEmpty(value) ? false : value.matches(".*["+charAppointed+"]+.*");
    }

    public static boolean isEmail(String value) {
        return isEmpty(value) ? false : value.matches(".+@\\w+\\.\\w+");
    }

    public static boolean isTableColumn(String value) {
        if (isEmpty(value) || hasLowerCase(value) ) {
            return false;
        }
        return true;
    }

    public static String convertColumnName(String value) {
        if (isEmpty(value)) {
            return null;
        } else if (!value.contains("_")) {
            return value.toLowerCase();
        }
        String[] headNSuf = value.split("_");
        StringBuilder builder = new StringBuilder();
        builder.append(headNSuf[0].toLowerCase());
        for (int i = 1; i < headNSuf.length; i++) {
            builder.append(toUpperInitial(headNSuf[i]));
        }
        return builder.toString();
    }

    public static String convertToTableColumn(String value) {
        if (isEmpty(value)) {
            return null;
        } else if (value.contains("_") || !hasLowerCase(value)) {
            return value.toUpperCase();
        }
        StringBuilder column = new StringBuilder();
        String[] src = convertToArray(value);
        Arrays.stream(src).forEach((s) -> {
            if(hasUpperCase(s)) {
                column.append("_");
            }
            column.append(s.toUpperCase());
        });
        if (column.indexOf("_") == 0)
            column.deleteCharAt(0);
        return column.toString();
    }

    public static String getPartOf(String value, int start, int end) {
        if (isEmpty(value)) {
            return null;
        }

        if (start <= end && end < value.length()) {
            return value.substring(start, end + 1);
        }
        return null;
    }

    public static String[] convertToArray(String value) {
        if (value.isBlank())
            return null;
        String array[] = new String[value.length()];
        for (int i = 0; i < value.length(); i++) {
            array[i] = String.valueOf(value.charAt(i));
        }
        return array;
    }

    public static String getFileSuffix(String fullfileName) {
        if (StringUtil.isEmpty(fullfileName) || !fullfileName.contains(".")) {
            return null;
        }
        return fullfileName.substring(fullfileName.lastIndexOf(".")+1);
    }

    public static String getFilePrefix(String fullfileName) {
        if (StringUtil.isEmpty(fullfileName) || !fullfileName.contains(".")) {
            return fullfileName;
        }
        return fullfileName.substring(0,fullfileName.lastIndexOf("."));
    }

    public static String getFileNameFromPath(String path) {
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        String[] splitStr = null;
        if (path.contains("/")) {
            splitStr = path.split("/");
        } else if (path.contains("\\")) {
            splitStr = path.split("\\\\");
        }
        if (splitStr != null && splitStr.length > 0) {
            return splitStr[splitStr.length - 1];
        } else {
            return null;
        }
    }

    public static int getAlphaIndex(String alpha) {
        if (isAlpha(alpha) && alpha.length() == 1) {
            return alphabet.indexOf(alpha) + 1;
        }
        return 0;
    }

    public static String appendFileName(String dir, String fileName) {
        if (!isEmpty(dir) && !isEmpty(fileName)) {
            dir = dir.replaceAll("\\\\", "/");
            if (dir.endsWith("/")) {
                return dir + fileName;
            } else {
                return dir + "/" + fileName;
            }
        }
        return null;
    }

    public static String deleteCharAt(String src, int index) {
        if (!isEmpty(src)) {
            StringBuilder builder = new StringBuilder(src);
            return builder.deleteCharAt(index).toString();
        }
        return null;
    }

    public static String addSingleQuo(String src) {
        return "'" + src + "'";
    }
}
