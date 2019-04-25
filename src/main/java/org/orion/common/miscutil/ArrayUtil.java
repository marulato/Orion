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

    public static<E> List<E> convertToList(Set<E> set) {
        List<E> list = new ArrayList<>();
        if (set != null && set.size() > 0) {
            Iterator<E> iterator = set.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        return list;
    }

    public static<E> Set<E> convertToSet(List<E> list) {
        Set<E> set = new HashSet<>();
        if (list != null && list.size() > 0) {
            for (E e : list) {
                set.add(e);
            }
        }
        return set;
    }

    public static <T> List<T> addElements(List<T> list , T... ele) {
        if (list != null && !Nullable.isNull(ele)) {
            for (T e : ele) {
                list.add(e);
            }
        }
        return list;
    }

    public static <T> Set<T> addElements(Set<T> set , T... ele) {
        if (set != null && !Nullable.isNull(ele)) {
            for (T e : ele) {
                set.add(e);
            }
        }
        return set;
    }

    public static <K, V> Map<K, V> addElements(Map<K, V> map, Object...ele) {
        if (map != null && !Nullable.isNull(ele) && ele.length % 2 == 0) {
            for(int i = 0; i < ele.length; i++) {
                map.put((K)ele[i], (V)ele[i + 1]);
            }
        }
        return map;
    }

    public static <E> void filter(Collection<E> collection, Predicate<E> predicate) {
        if (collection != null) {
            collection.stream().filter(predicate);
        }
    }

}
