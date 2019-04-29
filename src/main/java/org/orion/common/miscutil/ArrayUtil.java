package org.orion.common.miscutil;

import java.util.*;
import java.util.function.Predicate;

public class ArrayUtil {

    public static boolean contains(Object[] arr,  Object... item) {
        if (!Nullable.isNull(arr)) {
            if (Nullable.isNull(item)) {
                return false;
            }
            List<Object> list = Arrays.asList(arr);
            for(Object obj : item) {
                if (!list.contains(obj))
                    return false;
            }
        }
        return true;
    }

    public static int binarySearch(int[] arr, int key) {
        Arrays.sort(arr);
        return Arrays.binarySearch(arr, key);
    }
    public static int binarySearch(byte[] arr, byte key) {
        Arrays.sort(arr);
        return Arrays.binarySearch(arr, key);
    }

    public static int binarySearch(char[] arr, char key) {
        Arrays.sort(arr);
        return Arrays.binarySearch(arr, key);
    }

    public static int binarySearch(double[] arr, double key) {
        Arrays.sort(arr);
        return Arrays.binarySearch(arr, key);
    }

    public static int binarySearch(long[] arr, long key) {
        Arrays.sort(arr);
        return Arrays.binarySearch(arr, key);
    }

    public static int binarySearch(String[] arr, String key) {
        Arrays.sort(arr);
        return Arrays.binarySearch(arr, key);
    }

    public static Object[] get(Object[] arr, int start, int end) {
        if (!Nullable.isNull(arr) && start < end && end < arr.length) {
            Object[] copy = new Object[end - start + 1];
            int index = 0;
            for (int i = start; i <= end; i++) {
                copy[index ++] = arr[i];
            }
            return copy;
        }
        return null;
    }

    public static int[] get(int[] arr, int start, int end) {
        if (!Nullable.isNull(arr) && start < end && end < arr.length) {
            int[] copy = new int[end - start + 1];
            int index = 0;
            for (int i = start; i <= end; i++) {
                copy[index ++] = arr[i];
            }
            return copy;
        }
        return null;
    }

    public static String[] get(String[] arr, int start, int end) {
        if (!Nullable.isNull(arr) && start < end && end < arr.length) {
            String[] copy = new String[end - start + 1];
            int index = 0;
            for (int i = start; i <= end; i++) {
                copy[index ++] = arr[i];
            }
            return copy;
        }
        return null;
    }

    public static double[] get(double[] arr, int start, int end) {
        if (!Nullable.isNull(arr) && start < end && end < arr.length) {
            double[] copy = new double[end - start + 1];
            int index = 0;
            for (int i = start; i <= end; i++) {
                copy[index ++] = arr[i];
            }
            return copy;
        }
        return null;
    }

    public static String[] toStringArray(List<String> list) {
        if (!Nullable.isNull(list)) {
            String[] array = new String[list.size()];
            list.toArray(array);
            return array;
        }
        return null;
    }

}
