package org.orion.common.miscutil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Sorting {

    public static void sortInteger(List<Integer> list, boolean stl) {
        if (list != null) {
            if (stl) {
                Collections.sort(list, (o1, o2) -> {
                    return o1 - o2;
                });
            } else {
                Collections.sort(list, (o1, o2) -> {
                    return o2 - o1;
                });
            }
        }
    }

    public static void sortDecimal(List<Double> list, boolean stl) {
        if (list != null) {
            if (stl) {
                Collections.sort(list, (o1, o2) -> {
                    return (int) (o1 - o2);
                });
            } else {
                Collections.sort(list, (o1, o2) -> {
                    return (int) (o2 - o1);
                });
            }
        }
    }

    public static void sortDate(List<Date> list, boolean stl) {
        if (list != null) {
            if (stl) {
                Collections.sort(list, (o1, o2) -> {
                    if (o1.before(o2))
                        return 1;
                    else if(o1.after(o2))
                        return -1;
                    else
                        return 0;
                });
            } else {
                Collections.sort(list, (o1, o2) -> {
                    if (o1.before(o2))
                        return -1;
                    else if(o1.after(o2))
                        return 1;
                    else
                        return 0;
                });
            }
        }
    }
}
