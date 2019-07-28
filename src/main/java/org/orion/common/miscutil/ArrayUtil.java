package org.orion.common.miscutil;

import java.util.Arrays;
import java.util.List;

public class ArrayUtil {

    public static boolean isEmpty(String[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }


    public static boolean contains(Object[] arr,  Object... item) {
        if (arr != null && item != null) {
            List<Object> list = Arrays.asList(arr);
            List<Object> items = Arrays.asList(item);
            return list.containsAll(items);
        }
        return false;
    }


    public static int binarySearch(String[] arr, String key) {
        Arrays.sort(arr);
        return Arrays.binarySearch(arr, key);
    }

    public static Object[] get(Object[] arr, int start, int end) {
        if (arr != null && start < end && end < arr.length) {
            Object[] copy = new Object[end - start + 1];
            int index = 0;
            for (int i = start; i <= end; i++) {
                copy[index ++] = arr[i];
            }
            return copy;
        }
        return null;
    }

    public static String[] get(String[] arr, int start, int end) {
        if (arr != null && start < end && end < arr.length) {
            String[] copy = new String[end - start + 1];
            int index = 0;
            for (int i = start; i <= end; i++) {
                copy[index ++] = arr[i];
            }
            return copy;
        }
        return null;
    }

    public static byte[] get(byte[] arr, int start, int end) {
        if (arr != null && start < end && end < arr.length) {
            byte[] copy = new byte[end - start + 1];
            int index = 0;
            for (int i = start; i <= end; i++) {
                copy[index ++] = arr[i];
            }
            return copy;
        }
        return null;
    }

    public static String[] toStringArray(List<String> list) {
        if (list != null) {
            String[] array = new String[list.size()];
            list.toArray(array);
            return array;
        }
        return null;
    }

    public static Object[] joint(Object[]... arrays) {
        Object[] newArray = null;
        int totalLength = 0;
        if (arrays != null && arrays.length > 0) {
            for (Object[] e : arrays) {
                if (e != null && e.length > 0)
                    totalLength += e.length;
            }
            newArray = new Object[totalLength];
            int index = 0;
            for (Object[] e : arrays) {
                if (e != null && e.length > 0) {
                    for (int i = 0; i < e.length; i++) {
                        newArray[index++] = e[i];
                    }
                }
            }
        }
        return newArray;
    }

}
