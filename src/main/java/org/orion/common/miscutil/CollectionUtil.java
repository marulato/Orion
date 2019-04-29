package org.orion.common.miscutil;

import org.apache.poi.ss.formula.functions.T;

import java.util.*;
import java.util.function.Predicate;

public class CollectionUtil {

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

  public static <T> Collection<T> addElements(Collection<T> list , T... ele) {
        if (list != null && !Nullable.isNull(ele)) {
            for (T e : ele) {
                list.add(e);
            }
        }
        return list;
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

    public static <E> List<E> initialList(E...ele) {
        List<E> list = new ArrayList<>();
        addElements(list, ele);
        return list;
    }
}
