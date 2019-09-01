package org.akita.sql.utility;

public class NameUtils {

    public static boolean isEmpty(String src) {
        return src == null || src.isBlank();
    }

    public static boolean hasUnderline(String property) {
        return property.contains("_");
    }

    public static String toTitleCase(String property) {
        char[] chars = property.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static boolean hasTitleCase(String property) {
        char[] chars = property.toCharArray();
        for (char ch : chars) {
            if (Character.isUpperCase(ch)) return true;
        }
        return false;
    }

    public static String [] splitByUpperCase(String property) {
        char[] chars = property.toCharArray();
        StringBuilder fragment = new StringBuilder();
        for (char ch : chars) {
            if (ch >= 'A' && ch <= 'Z') {
                fragment.append("_");
            }
            fragment.append(ch);
        }
        return fragment.toString().split("_");
    }

}
